package example.matcher;

import java.util.PrimitiveIterator;
import java.util.regex.Pattern;

public class AntPathMatcher {

	private final Pattern pattern;

	public AntPathMatcher(String pattern) {
		this.pattern = Pattern.compile(new PatternParser(pattern).parse());
	}

	public boolean matches(String path) {
		return pattern.matcher(path).matches();
	}

	static class PatternParser {

		private final PrimitiveIterator.OfInt iterator;
		private final String pattern;

		public PatternParser(String pattern) {
			this.pattern = pattern;
			this.iterator = pattern.codePoints().iterator();
		}

		public String parse() {
			StringBuilder buf = new StringBuilder();
			boolean asterisk = false;
			boolean doubleAsterisk = false;
			while (iterator.hasNext()) {
				int codePoint = iterator.nextInt();
				if (asterisk) {
					asterisk = false;
					if (codePoint == '*') {
						doubleAsterisk = true;
						continue;
					}
					buf.append("[^/]+");
				}
				if (doubleAsterisk) {
					doubleAsterisk = false;
					buf.append(".*");
					if (codePoint == '/') {
						continue;
					}
				}
				switch (codePoint) {
				case '?':
					buf.append("[^/]");
					break;
				case '*':
					asterisk = true;
					break;
				case '.':
					buf.append("\\.");
					break;
				default:
					buf.appendCodePoint(codePoint);
					break;
				}
			}
			if (asterisk) {
				buf.append("[^/]+");
			}
			if (doubleAsterisk || pattern.endsWith("/")) {
				buf.append(".*");
			}
			return buf.toString();
		}
	}
}
