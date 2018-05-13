package parser.expression;

public abstract class Parser {

    protected static final char EOF = (char) -1;
    private final String input;
    private int position;

    public Parser(final String input) {
        this.input = input;
    }

    protected char getChar() {
        if (position < input.length()) {
            return input.charAt(position);
        }
        return EOF;
    }

    protected void consume() {
        position++;
    }

    protected void match(final char expected) {
        if (getChar() == expected) {
            consume();
        } else {
            throw new ParseException();
        }
    }

    protected int getPosition() {
        return position;
    }

    protected void setPosition(final int position) {
        this.position = position;
    }
}
