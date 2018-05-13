package parser.combinator;

public interface Parser {

    Object parse(ParseContext context);

    default Object parse(final String input) {
        return parse(new ParseContext(input));
    }

    default Parser to(final Converter converter) {
        return new WithConverter(this, converter);
    }

    default Parser dropBothEnds() {
        return new DropBothEnds(this);
    }

    default Parser flatten() {
        return new Flatten(this);
    }

    default Parser right() {
        return new Right(this);
    }

    default Parser left() {
        return new Left(this);
    }
}
