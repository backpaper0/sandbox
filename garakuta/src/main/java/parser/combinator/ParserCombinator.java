package parser.combinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ParserCombinator {

	public Parser literal(final String text) {
		return new LiteralParser(text);
	}

	public Parser repeat(final Parser parser) {
		return new RepeatParser(parser);
	}

	public Parser option(final Parser parser) {
		return new OptionParser(parser);
	}

	public Parser or(final Parser... parsers) {
		return new OrParser(parsers);
	}

	public Parser and(final Parser... parsers) {
		return new AndParser(parsers);
	}

	public Parser lazy(final Supplier<Parser> factory) {
		return new LazyParser(factory);
	}

	public Parser eof() {
		return new EofParser();
	}

	private static class LiteralParser implements Parser {

		private final String text;

		public LiteralParser(final String text) {
			this.text = text;
		}

		@Override
		public Object parse(final ParseContext context) {
			for (final char expected : text.toCharArray()) {
				context.match(expected);
			}
			return text;
		}

		@Override
		public String toString() {
			return String.format("'%s'", text);
		}
	}

	private static class RepeatParser implements Parser {

		private final Parser parser;

		public RepeatParser(final Parser parser) {
			this.parser = parser;
		}

		@Override
		public Object parse(final ParseContext context) {
			final List<Object> list = new ArrayList<>();
			for (;;) {
				final int savePoint = context.getPosition();
				try {
					list.add(parser.parse(context));
				} catch (final ParseException e) {
					context.setPosition(savePoint);
					return list;
				}
			}
		}

		@Override
		public String toString() {
			return String.format("{ %s }", parser);
		}
	}

	private static class OptionParser implements Parser {

		private final Parser parser;

		public OptionParser(final Parser parser) {
			this.parser = parser;
		}

		@Override
		public Object parse(final ParseContext context) {
			final List<Object> list = new ArrayList<>();
			final int savePoint = context.getPosition();
			try {
				list.add(parser.parse(context));
			} catch (final ParseException e) {
				context.setPosition(savePoint);
			}
			return list;
		}

		@Override
		public String toString() {
			return String.format("[ %s ]", parser);
		}
	}

	private static class OrParser implements Parser {

		private final Parser[] parsers;

		public OrParser(final Parser[] parsers) {
			this.parsers = parsers;
		}

		@Override
		public Object parse(final ParseContext context) {
			for (final Parser parser : parsers) {
				final int savePoint = context.getPosition();
				try {
					return parser.parse(context);
				} catch (final ParseException e) {
					context.setPosition(savePoint);
				}
			}
			throw new ParseException(context,
					String.format("no rule matched in %s", Arrays.asList(parsers)));
		}

		@Override
		public String toString() {
			return Arrays.stream(parsers).map(Objects::toString)
					.collect(Collectors.joining(" | ", "(", ")"));
		}
	}

	private static class AndParser implements Parser {

		private final Parser[] parsers;

		public AndParser(final Parser[] parsers) {
			this.parsers = parsers;
		}

		@Override
		public List<Object> parse(final ParseContext context) {
			final List<Object> list = new ArrayList<>();
			for (final Parser parser : parsers) {
				list.add(parser.parse(context));
			}
			return list;
		}

		@Override
		public String toString() {
			return Arrays.stream(parsers).map(Objects::toString)
					.collect(Collectors.joining(" ", "(", ")"));
		}
	}

	private static class EofParser implements Parser {

		@Override
		public Object parse(final ParseContext context) {
			context.match((char) -1);
			return null;
		}
	}

	private static class LazyParser implements Parser {

		private final Supplier<Parser> factory;
		private Parser parser;

		public LazyParser(final Supplier<Parser> factory) {
			this.factory = factory;
		}

		@Override
		public Object parse(final ParseContext context) {
			if (parser == null) {
				parser = factory.get();
			}
			return parser.parse(context);
		}

		@Override
		public String toString() {
			return "<lazy>";
		}
	}
}
