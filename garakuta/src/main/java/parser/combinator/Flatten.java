package parser.combinator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Flatten implements Parser {

	private final Parser parser;

	Flatten(final Parser parser) {
		this.parser = parser;
	}

	@Override
	public Object parse(final ParseContext context) {
		final List<Object> list = (List<Object>) parser.parse(context);
		return list.stream().flatMap(x -> {
			if (x instanceof List) {
				return ((List<?>) x).stream();
			}
			return Stream.of(x);
		}).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return parser.toString();
	}
}
