package csv.lex;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

class CsvLexerTest {

    @Test
    void test() throws Exception {
        final String s = "foo,\"b\"\"a,r\"\r\nbaz,qux\r\n,\"\"";
        final Reader in = new StringReader(s);
        final CsvLexer lexer = new CsvLexer(in);
        assertEquals(CsvToken.field("foo"), lexer.next());
        assertEquals(CsvToken.SEPARATOR, lexer.next());
        assertEquals(CsvToken.field("b\"a,r"), lexer.next());
        assertEquals(CsvToken.LINE_BREAK, lexer.next());
        assertEquals(CsvToken.field("baz"), lexer.next());
        assertEquals(CsvToken.SEPARATOR, lexer.next());
        assertEquals(CsvToken.field("qux"), lexer.next());
        assertEquals(CsvToken.LINE_BREAK, lexer.next());
        assertEquals(CsvToken.SEPARATOR, lexer.next());
        assertEquals(CsvToken.field(""), lexer.next());
        assertEquals(CsvToken.EOF, lexer.next());
    }
}
