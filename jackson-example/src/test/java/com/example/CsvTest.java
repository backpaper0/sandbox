package com.example;

import static org.hamcrest.CoreMatchers.*;
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

    @Test
    public void testArrayColumn() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        //        final CsvSchema preSchema = mapper.typedSchemaFor(User.class);
        //        final int index = preSchema.column("roles").getIndex();
        //        final CsvSchema schema = preSchema.rebuild()
        //                .replaceColumn(index, new Column(index, "roles", ColumnType.ARRAY, ","))
        //                .setUseHeader(true)
        //                .setLineSeparator("\r\n")
        //                .build();

        final CsvSchema schema = CsvSchema.builder()
                .addColumn("username")
                .addArrayColumn("roles", ",")
                .setUseHeader(true)
                .setLineSeparator("\r\n")
                .build();

        final User user1 = new User("hoge", "FOO");
        final User user2 = new User("fuga", "FOO", "BAR", "BAZ");
        final User user3 = new User("piyo");

        final StringWriter out = new StringWriter();
        try (final SequenceWriter writer = mapper.writerFor(User.class).with(schema)
                .writeValues(out)) {
            writer.write(user1);
            writer.write(user2);
            writer.write(user3);
        }
        final String csv = out.toString();
        System.out.println(csv);
        final StringReader in = new StringReader(csv);
        try (MappingIterator<User> iterator = mapper.readerFor(User.class).with(schema)
                .readValues(in)) {
            final User user4 = iterator.next();
            final User user5 = iterator.next();
            final User user6 = iterator.next();
            System.out.println(user4);
            System.out.println(user5);
            System.out.println(user6);

            assertThat(user4, is(user1));
            assertThat(user5, is(user2));
            assertThat(user6, is(user3));
            assertThat(iterator.hasNext(), is(false));
        }
    }
}
