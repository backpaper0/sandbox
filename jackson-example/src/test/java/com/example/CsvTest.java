package com.example;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType;

public class CsvTest {

    @Test
    public void test() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        final CsvSchema schema = CsvSchema.builder()
                .addColumn("id", ColumnType.NUMBER)
                .addColumn("content", ColumnType.STRING)
                .setUseHeader(true)
                .setLineSeparator("\r\n")
                .build();

        final StringWriter out = new StringWriter();
        try (final SequenceWriter writer = mapper.writer().with(schema).writeValues(out)) {
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
    public void testTsv() throws Exception {
        final CsvMapper mapper = new CsvMapper();

        final CsvSchema schema = CsvSchema.builder()
                .addColumn("id", ColumnType.NUMBER)
                .addColumn("content", ColumnType.STRING)
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
}
