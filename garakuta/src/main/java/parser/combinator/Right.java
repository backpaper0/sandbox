package parser.combinator;

import java.util.List;

class Right implements Parser {

    private final Parser parser;

    Right(final Parser parser) {
        this.parser = parser;
    }

    @Override
    public Object parse(final ParseContext context) {
        final List<Object> list = (List<Object>) parser.parse(context);
        if (list.size() != 2) {
            throw new ParseException(context, "result size must be 2");
        }
        return list.get(1);
    }

    @Override
    public String toString() {
        return parser.toString();
    }
}
