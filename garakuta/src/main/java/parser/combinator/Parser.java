package parser.combinator;

public interface Parser {

    Object parse(ParseContext context);

    default Object parse(final String input) {
        return parse(new ParseContext(input));
    }

    default Parser to(final Converter converter) {
        return new WithConverter(this, converter);
    }

    class WithConverter implements Parser {

        private final Parser parser;
        private final Converter converter;

        private WithConverter(final Parser parser, final Converter converter) {
            this.parser = parser;
            this.converter = converter;
        }

        @Override
        public Object parse(final ParseContext context) {
            return converter.to(parser.parse(context));
        }

        @Override
        public Parser to(final Converter converter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return parser.toString();
        }
    }
}
