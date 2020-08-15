package example;

import static org.junit.jupiter.api.Assertions.*;

import java.text.BreakIterator;

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
}
