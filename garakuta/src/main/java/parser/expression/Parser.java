package parser.expression;

import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public abstract class Parser {

    protected static final char EOF = (char) -1;
    private final String input;
    private int position;

    public Parser(final String input) {
        this.input = input;
    }

    private char getChar() {
        if (position < input.length()) {
            return input.charAt(position);
        }
        return EOF;
    }

    private void consume() {
        position++;
    }

    protected char match(final char expected) {
        final char c = getChar();
        if (c == expected) {
            consume();
            return c;
        }
        throw new ParseException();
    }

    protected char match(final IntPredicate expected) {
        final char c = getChar();
        if (expected.test(c)) {
            consume();
            return c;
        }
        throw new ParseException();
    }

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
