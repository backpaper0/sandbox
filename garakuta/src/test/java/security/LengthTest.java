package security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import security.Length.Status;

class LengthTest {

	@Test
	void setBytes() throws Exception {
		final Length len = new Length(8);
		len.iValue = 0b10110011100011110000111110000011;
		final byte[] bs = new byte[10];
		len.writeTo(bs, 1);
		final byte[] expected = {
				0,
				0,
				0,
				0,
				0,
				(byte) 0b10110011,
				(byte) 0b10001111,
				(byte) 0b00001111,
				(byte) 0b10000011,
				0 };
		assertArrayEquals(expected, bs);
	}

	@Test
	void increment() throws Exception {
		final Length len = new Length(8);
		len.iValue = Integer.MAX_VALUE - 9;

		assertEquals(Integer.MAX_VALUE - 9, len.iValue);
		assertEquals(0L, len.lValue);
		assertEquals(Status.INT, len.status);

		len.increment();
		assertEquals(Integer.MAX_VALUE - 1, len.iValue);
		assertEquals(0L, len.lValue);
		assertEquals(Status.INT, len.status);

		len.increment();
		assertEquals(Integer.MAX_VALUE - 1, len.iValue);
		assertEquals(Integer.MAX_VALUE + 7L, len.lValue);
		assertEquals(Status.LONG, len.status);
	}
}
