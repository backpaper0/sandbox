package com.example;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvTest {

    @Test
    public void testWrite() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        final CsvSchema schema = mapper.schemaFor(Message.class)
                .rebuild()
                .setUseHeader(true)
                .setLineSeparator("\r\n")
                .build();

        final StringWriter out = new StringWriter();
        try (final SequenceWriter writer = mapper.writerFor(Message.class).with(schema)
                .writeValues(out)) {
            writer.write(new Message(123, "foo"));
            writer.write(new Message(456, "bar"));
            writer.write(new Message(789, "baz"));
        }

        final String csv = out.toString();
        System.out.println(csv);
        assertEquals("id,content\r\n"
                + "123,foo\r\n"
                + "456,bar\r\n"
                + "789,baz\r\n", csv);
    }

    @Test
    public void testWriteTsv() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        final CsvSchema schema = mapper.schemaFor(Message.class)
                .rebuild()
                .setUseHeader(true)
                .setLineSeparator("\r\n")
                .setColumnSeparator('\t')
                .build();

        final StringWriter out = new StringWriter();
        try (final SequenceWriter writer = mapper.writer().with(schema).writeValues(out)) {
            writer.write(new Message(123, "foo"));
            writer.write(new Message(456, "bar"));
            writer.write(new Message(789, "baz"));
        }

        final String tsv = out.toString();
        System.out.println(tsv);
        assertEquals("id\tcontent\r\n"
                + "123\tfoo\r\n"
                + "456\tbar\r\n"
                + "789\tbaz\r\n", tsv);
    }

    @Test
    public void testRead() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        final CsvSchema schema = mapper.schemaFor(Message.class)
                .rebuild()
                .setUseHeader(true)
                .setLineSeparator("\r\n")
                .build();

        final StringReader in = new StringReader("id,content\r\n"
                + "123,foo\r\n"
                + "456,bar\r\n"
                + "789,baz\r\n");

        try (MappingIterator<Message> iterator = mapper.readerFor(Message.class).with(schema)
                .readValues(in)) {
            final Message message1 = iterator.next();
            final Message message2 = iterator.next();
            final Message message3 = iterator.next();
            System.out.println(message1);
            System.out.println(message2);
            System.out.println(message3);
            assertEquals(new Message(123, "foo"), message1);
            assertEquals(new Message(456, "bar"), message2);
            assertEquals(new Message(789, "baz"), message3);
            assertFalse(iterator.hasNext());
        }
    }
}
