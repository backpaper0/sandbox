package example;

import static org.junit.jupiter.api.Assertions.*;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class UnicodeExample {

	@Test
	void surrogatePair() {
		String s = "𩸽"; //ホッケ
		assertEquals(2, s.length());
		assertEquals(1, s.codePointCount(0, s.length()));

		char c0 = s.charAt(0);
		char c1 = s.charAt(1);

		assertTrue(Character.isSurrogatePair(c0, c1));

		assertTrue(Character.isHighSurrogate(c0));
		assertFalse(Character.isLowSurrogate(c0));

		assertFalse(Character.isHighSurrogate(c1));
		assertTrue(Character.isLowSurrogate(c1));
	}

	@Test
	void notSurrogatePair() {
		String s = "鮭";
		assertEquals(1, s.length());
		assertEquals(1, s.codePointCount(0, s.length()));

		char c0 = s.charAt(0);

		assertFalse(Character.isHighSurrogate(c0));
		assertFalse(Character.isLowSurrogate(c0));
	}

	@Test
	void sentenceContainsSurrogatePair() throws Exception {
		//ほっけをたべたい
		//見た目上の文字数は6
		final String s = "𩸽を食べたい";
		assertEquals(7, s.length());
		assertEquals(6, s.codePointCount(0, s.length()));
		assertEquals(6, s.codePoints().count());

		assertEquals("𩸽", new String(new int[] { s.codePointAt(0) }, 0, 1));

		BreakIterator it = BreakIterator.getCharacterInstance();
		it.setText(s);

		int count = 0;
		while (it.next() != BreakIterator.DONE) {
			count++;
		}
		assertEquals(6, count);
	}

	@Test
	void emojiWithZWJ() throws Exception {
		String s0 = "🏳️‍🌈";
		String s1 = "👩‍❤️‍👨";
		String s2 = "👨‍👩‍👧‍👦";
		String s3 = "🏄‍♂️";
		String s4 = "👯‍♀️";
		String s5 = "🧞‍♂️";
		String s6 = "👩‍💻";

		//見た目上の文字数は7(MacBook Pro/Eclipse上で目視)
		String s = s0 + s1 + s2 + s3 + s4 + s5 + s6;
		System.out.println(s);
		Consumer<String> c = a -> System.out.printf("%s    %s%n", a,
				a.codePoints().mapToObj(b -> String.format("%5X", b))
						.collect(Collectors.joining(", ")));
		c.accept(s0);
		c.accept(s1);
		c.accept(s2);
		c.accept(s3);
		c.accept(s4);
		c.accept(s5);
		c.accept(s6);

		//char数、コードポイント数ともに4ではない
		assertEquals(45, s.length());
		assertEquals(32, s.codePointCount(0, s.length()));

		{
			BreakIterator it = BreakIterator.getCharacterInstance();
			it.setText(s);
			int count = 0;
			while (it.next() != BreakIterator.DONE) {
				count++;
			}
			//結合前の各絵文字とゼロ幅接合子(ZWJ)がカウントされている
			assertEquals(27, count);
		}

		{
			BreakIterator it = BreakIterator.getCharacterInstance();
			it.setText(s);
			int count = 0;
			int index = 0;
			while (it.next() != BreakIterator.DONE) {
				//ZWJとそれに続く文字はカウントしないことで見た目上の数をカウントできる
				if (s.codePointAt(index) == 0x200d) {
					it.next();
				} else {
					count++;
				}
				index = it.current();
			}
			//見た目上の数をカウントできた
			assertEquals(7, count);
		}
	}

	@Test
	void normilizer() throws Exception {
		UnaryOperator<String> f = s -> s.codePoints().mapToObj(a -> String.format("%x", a))
				.collect(Collectors.joining(" "));
		List<String> strings = List.of("か", "が", "ガ", "ｶﾞ", "ｶ", "㍻", "㌔");
		for (String string : strings) {
			System.out.printf("%s (%s)%n", string, f.apply(string));
			for (Normalizer.Form form : Normalizer.Form.values()) {
				String normilized = Normalizer.normalize(string, form);
				System.out.printf("%s: %s (%s)%n", form, normilized, f.apply(normilized));
			}
			System.out.printf("%n");
		}
	}

	@Test
	void control() throws Exception {
		for (int codePoint = 0; codePoint < 0xff; codePoint++) {
			String s = new String(new int[] { codePoint }, 0, 1);
			if (Character.isISOControl(codePoint)) {
				System.out.printf("%02x [制御文字]%n", codePoint);
			} else {
				System.out.printf("%02x %s%n", codePoint, s);
			}
		}
	}
}
