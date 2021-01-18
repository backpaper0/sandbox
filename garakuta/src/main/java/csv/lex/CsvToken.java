package csv.lex;

import java.util.Objects;

public final class CsvToken {

	public static final CsvToken SEPARATOR = new CsvToken(CsvTokenType.SEPARATOR, "<SEPARATOR>");
	public static final CsvToken LINE_BREAK = new CsvToken(CsvTokenType.LINE_BREAK, "<LINE_BREAK>");
	public static final CsvToken EOF = new CsvToken(CsvTokenType.EOF, "<EOF>");

	private final CsvTokenType type;
	public final String text;

	private CsvToken(final CsvTokenType type, final String text) {
		this.type = Objects.requireNonNull(type);
		this.text = Objects.requireNonNull(text);
	}

	public static CsvToken field(final String text) {
		return new CsvToken(CsvTokenType.FIELD, text);
	}

	public boolean isEof() {
		return type == CsvTokenType.EOF;
	}

	public boolean isLineBreak() {
		return type == CsvTokenType.LINE_BREAK;
	}

	public boolean isSeparator() {
		return type == CsvTokenType.SEPARATOR;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, text);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final CsvToken other = (CsvToken) obj;
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
