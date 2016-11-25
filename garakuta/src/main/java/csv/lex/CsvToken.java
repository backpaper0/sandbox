package csv.lex;

import java.util.Objects;

public final class CsvToken {

    public static final CsvToken SEPARATOR = new CsvToken(CsvTokenType.SEPARATOR, "<SEPARATOR>");
    public static final CsvToken LINE_BREAK = new CsvToken(CsvTokenType.LINE_BREAK, "<LINE_BREAK>");
    public static final CsvToken EOF = new CsvToken(CsvTokenType.EOF, "<EOF>");

    private final CsvTokenType type;
    public final String text;

    private CsvToken(CsvTokenType type, String text) {
        this.type = Objects.requireNonNull(type);
        this.text = Objects.requireNonNull(text);
    }

    public static CsvToken field(String text) {
        return new CsvToken(CsvTokenType.FIELD, text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CsvToken other = (CsvToken) obj;
        return text.equals(other.text) && type == other.type;
    }

    @Override
    public String toString() {
        return String.format("%1$s(%2$s)", type, text);
    }

    private enum CsvTokenType {
        FIELD, SEPARATOR, LINE_BREAK, EOF
    }
}
