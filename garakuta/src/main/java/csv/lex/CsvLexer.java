package csv.lex;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

public class CsvLexer {

    private static final char EOF = (char) -1;
    private static final char CR = '\r';
    private static final char LF = '\n';
    private final Reader in;
    private final char separator;
    private char c;

    public CsvLexer(final Reader in, final char separator) throws IOException {
        this.separator = separator;
        this.in = Objects.requireNonNull(in);
        consume();
    }

    public CsvLexer(final Reader in) throws IOException {
        this(in, ',');
    }

    private void consume() throws IOException {
        do {
            c = (char) in.read();
        } while (c == CR);
    }

    public CsvToken next() throws IOException {
        if (c == EOF) {
            return CsvToken.EOF;

        } else if (c == separator) {
            consume();
            return CsvToken.SEPARATOR;

        } else if (c == LF) {
            consume();
            return CsvToken.LINE_BREAK;

        } else if (c == '"') {
            final StringBuilder buf = new StringBuilder();
            while (true) {
                consume();
                if (c == '"') {
                    consume();
                    if (c != '"') {
                        break;
                    }
                }
                buf.append(c);
            }
            return CsvToken.field(buf.toString());
        }

        final StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (c != EOF && c != separator && c != LF);
        return CsvToken.field(buf.toString());
    }
}
