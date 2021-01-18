package parser.combinator;

public class ParseContext {

	private static final char EOF = (char) -1;
	private final String input;
	private int position;

	public ParseContext(final String input) {
		this.input = input;
	}

	public void consume() {
		position++;
	}

	public char getChar() {
		if (position < input.length()) {
			return input.charAt(position);
		}
		return EOF;
	}

	public void match(final char expected) {
		if (getChar() == expected) {
			consume();
		} else {
			if (expected == EOF) {
				throw new ParseException(this,
						String.format("expected EOF, but was '%s'", getChar()));
			}
			throw new ParseException(this,
					String.format("expected '%s', but was '%s'", expected, getChar()));
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int savePoint) {
		this.position = savePoint;
	}

	public String getInput() {
		return input;
	}
}