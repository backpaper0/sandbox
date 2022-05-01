package example.和暦;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class 和暦の例 {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Gy年M月d日");

	public static void main(String[] args) {
		test(LocalDate.now());
		test(LocalDate.of(1872, 12, 31));
		test(LocalDate.of(1873, 1, 1));
		test(LocalDate.of(1912, 7, 29));
		test(LocalDate.of(1912, 7, 30));
		test(LocalDate.of(1926, 12, 24));
		test(LocalDate.of(1926, 12, 25));
		test(LocalDate.of(1989, 1, 6));
		test(LocalDate.of(1989, 1, 7));
		test(LocalDate.of(2019, 4, 30));
		test(LocalDate.of(2019, 5, 1));
		test(LocalDate.of(2999, 12, 31));
		/*
		https://github.com/openjdk/jdk/blob/jdk-17%2B35/src/java.base/share/classes/sun/util/calendar/LocalGregorianCalendar.java#L149-L150
		java -Djdk.calendar.japanese.supplemental.era="name=未知の元号,abbr=X,since=32503680000000" -cp target/classes example.和暦.和暦の例
		 */
		test(LocalDate.of(3000, 1, 1));
	}

	static void test(LocalDate localDate) {
		System.out.println(localDate);
		try {
			JapaneseDate date = JapaneseDate.from(localDate);
			System.out.println("  和暦表記：" + date.format(formatter));
			JapaneseEra era = date.getEra();
			Locale locale = Locale.JAPANESE;
			System.out.println("  元号：" + era.getDisplayName(TextStyle.FULL_STANDALONE, locale));
			System.out.println("  年：" + date.get(ChronoField.YEAR_OF_ERA));
		} catch (DateTimeException e) {
			System.out.println("  " + e.getMessage());
		}
		System.out.println();
	}
}
