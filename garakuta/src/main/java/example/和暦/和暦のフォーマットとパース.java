package example.和暦;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class 和暦のフォーマットとパース {

	private static Logger logger = Logger.getLogger(和暦のフォーマットとパース.class.getName());

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
	private static final Map<String, JapaneseEra> japaneseEraMap;
	static {
		japaneseEraMap = Arrays.stream(JapaneseEra.values())
				.collect(Collectors.toMap(
						a -> a.getDisplayName(TextStyle.NARROW, Locale.JAPAN),
						Function.identity()));
	}

	/**
	 * <ul>
	 * <li>R04/05/15 -&gt; 2022/05/15
	 * </ul>
	 * 
	 * @param japaneseDateAsString 和暦
	 * @return 西暦
	 */
	public static LocalDate 和暦のパース(String japaneseDateAsString) {
		LocalDate localDate = LocalDate.parse("00" + japaneseDateAsString.substring(1), formatter);
		String narrowJapaneseEra = japaneseDateAsString.substring(0, 1);
		JapaneseEra era = japaneseEraMap.get(narrowJapaneseEra);
		if (era == null) {
			return null;
		}
		int yearOfEra = localDate.getYear();
		int month = localDate.getMonthValue();
		int dayOfMonth = localDate.getDayOfMonth();
		JapaneseDate japaneseDate;
		try {
			japaneseDate = JapaneseDate.of(era, yearOfEra, month, dayOfMonth);
		} catch (DateTimeException e) {
			if (logger.isLoggable(Level.CONFIG)) {
				logger.config(e.getMessage());
			}
			return null;
		}
		return LocalDate.from(japaneseDate);
	}

	public static String 和暦へ変換(LocalDate localDate, Style style) {
		JapaneseDate japaneseDate;
		try {
			japaneseDate = JapaneseDate.from(localDate);
		} catch (DateTimeException e) {
			if (logger.isLoggable(Level.CONFIG)) {
				logger.config(e.getMessage());
			}
			return null;
		}
		return style.mapper.apply(japaneseDate);
	}

	public enum Style {
		_1("Gyy年MM月dd日"),
		_2(buildMapper("yy/MM/dd")),
		;

		private final Function<JapaneseDate, String> mapper;

		private static Function<JapaneseDate, String> buildMapper(String pattern) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			return date -> {
				String era = date.getEra().getDisplayName(TextStyle.NARROW, Locale.JAPAN);
				return era + date.format(formatter);
			};
		}

		Style(String pattern) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			this.mapper = date -> date.format(formatter);
		}

		Style(Function<JapaneseDate, String> mapper) {
			this.mapper = mapper;
		}
	}
}
