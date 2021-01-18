package parser.combinator;

class WithConverter implements Parser {

	private final Parser parser;
	private final Converter converter;

	WithConverter(final Parser parser, final Converter converter) {
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
