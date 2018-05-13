package parser.expression;

import java.util.Optional;
import java.util.function.Supplier;

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

    //    protected int getPosition() {
    //        return position;
    //    }
    //
    //    protected void setPosition(final int position) {
    //        this.position = position;
    //    }

    protected <T> Optional<T> tryParse(final Supplier<T> supplier) {
        final int savePoint = position;
        try {
            final T t = supplier.get();
            return Optional.of(t);
        } catch (final ParseException e) {
            position = savePoint;
            return Optional.empty();
        }
    }
}
