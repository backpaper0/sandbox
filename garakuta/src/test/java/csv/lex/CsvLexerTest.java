package csv.lex;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class CsvLexerTest {

    @Test
    public void test() throws Exception {
        String s = "foo,\"b\"\"a,r\"\r\nbaz,qux\r\n,\"\"";
        Reader in = new StringReader(s);
        CsvLexer lexer = new CsvLexer(in);
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
