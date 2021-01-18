package parser.combinator;

import java.util.List;

class DropBothEnds implements Parser {

	private final Parser parser;

	DropBothEnds(final Parser parser) {
		this.parser = parser;
	}

	@Override
	public Object parse(final ParseContext context) {
		final List<Object> list = (List<Object>) parser.parse(context);
		if (list.size() != 3) {
			throw new ParseException(context, "result size must be 3");
		}
		return list.get(1);
	}

	@Override
	public String toString() {
		return parser.toString();
	}
}
