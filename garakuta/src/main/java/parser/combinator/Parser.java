package parser.combinator;

public interface Parser {

    Object parse(ParseContext context);

    default Object parse(final String input) {
        return parse(new ParseContext(input));
    }

    default Parser to(final Converter converter) {
        return context -> converter.to(parse(context));
    }
}
