package csv.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import csv.lex.CsvLexer;
import csv.lex.CsvToken;

public class CsvParserTest {

    @Test
    public void test() throws Exception {
        List<CsvToken> tokens = new ArrayList<>();

        tokens.add(CsvToken.field("foo"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("bar"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("baz"));
        tokens.add(CsvToken.LINE_BREAK);

        tokens.add(CsvToken.field(""));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field(""));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field(""));
        tokens.add(CsvToken.LINE_BREAK);

        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.LINE_BREAK);

        tokens.add(CsvToken.EOF);

        Iterator<CsvToken> it = tokens.iterator();
        CsvLexer lexer = new CsvLexer(new StringReader("dummy")) {
            @Override
            public CsvToken next() throws IOException {
                return it.hasNext() ? it.next() : CsvToken.EOF;
            }
        };

        CsvParser parser = new CsvParser(lexer);
        assertEquals(Optional.of(Arrays.asList("foo", "bar", "baz")), parser.next());
        assertEquals(Optional.of(Arrays.asList("", "", "")), parser.next());
        assertEquals(Optional.of(Arrays.asList("", "", "")), parser.next());
        assertEquals(Optional.empty(), parser.next());
    }

    @Test
    public void test_line() throws Exception {
        List<CsvToken> tokens = new ArrayList<>();

        tokens.add(CsvToken.field("foo"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("bar"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("baz"));
        tokens.add(CsvToken.EOF);

        Iterator<CsvToken> it = tokens.iterator();
        CsvLexer lexer = new CsvLexer(new StringReader("dummy")) {
            @Override
            public CsvToken next() throws IOException {
                return it.hasNext() ? it.next() : CsvToken.EOF;
            }
        };

        CsvParser parser = new CsvParser(lexer);
        assertEquals(Optional.of(Arrays.asList("foo", "bar", "baz")), parser.next());
        assertEquals(Optional.empty(), parser.next());
    }

    @Test
    public void test_comma_only() throws Exception {
        List<CsvToken> tokens = new ArrayList<>();

        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.EOF);

        Iterator<CsvToken> it = tokens.iterator();
        CsvLexer lexer = new CsvLexer(new StringReader("dummy")) {
            @Override
            public CsvToken next() throws IOException {
                return it.hasNext() ? it.next() : CsvToken.EOF;
            }
        };

        CsvParser parser = new CsvParser(lexer);
        assertEquals(Optional.of(Arrays.asList("", "", "")), parser.next());
        assertEquals(Optional.empty(), parser.next());
    }

    @Test
    public void test_field_and_comma_repeate() throws Exception {
        List<CsvToken> tokens = new ArrayList<>();

        tokens.add(CsvToken.field("foo"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("bar"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("baz"));
        tokens.add(CsvToken.LINE_BREAK);
        tokens.add(CsvToken.EOF);

        Iterator<CsvToken> it = tokens.iterator();
        CsvLexer lexer = new CsvLexer(new StringReader("dummy")) {
            @Override
            public CsvToken next() throws IOException {
                return it.hasNext() ? it.next() : CsvToken.EOF;
            }
        };

        CsvParser parser = new CsvParser(lexer);
        assertEquals(Optional.of(Arrays.asList("foo", "", "bar", "", "baz")), parser.next());
        assertEquals(Optional.empty(), parser.next());
    }

    @Test
    public void test_field_and_comma_repeate_2() throws Exception {
        List<CsvToken> tokens = new ArrayList<>();

        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("foo"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("bar"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.field("baz"));
        tokens.add(CsvToken.SEPARATOR);
        tokens.add(CsvToken.EOF);

        Iterator<CsvToken> it = tokens.iterator();
        CsvLexer lexer = new CsvLexer(new StringReader("dummy")) {
            @Override
            public CsvToken next() throws IOException {
                return it.hasNext() ? it.next() : CsvToken.EOF;
            }
        };

        CsvParser parser = new CsvParser(lexer);
        assertEquals(Optional.of(Arrays.asList("", "foo", "", "bar", "", "baz", "")),
                parser.next());
        assertEquals(Optional.empty(), parser.next());
    }
}
