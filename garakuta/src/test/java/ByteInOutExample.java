import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ByteInOutExample {

	@Test
	void testInt() throws Exception {
		final long value1 = 1234567890987654321L;

		final byte[] bs = new byte[8];
		bs[0] = (byte) ((value1 >>> 56) & 0xff);
		bs[1] = (byte) ((value1 >>> 48) & 0xff);
		bs[2] = (byte) ((value1 >>> 40) & 0xff);
		bs[3] = (byte) ((value1 >>> 32) & 0xff);
		bs[4] = (byte) ((value1 >>> 24) & 0xff);
		bs[5] = (byte) ((value1 >>> 16) & 0xff);
		bs[6] = (byte) ((value1 >>> 8) & 0xff);
		bs[7] = (byte) ((value1 >>> 0) & 0xff);

		final long value2 = ((long) (bs[0] & 0xff) << 56L)
				+ ((long) (bs[1] & 0xff) << 48L)
				+ ((long) (bs[2] & 0xff) << 40L)
				+ ((long) (bs[3] & 0xff) << 32L)
				+ ((long) (bs[4] & 0xff) << 24L)
				+ ((long) (bs[5] & 0xff) << 16L) + ((long) (bs[6] & 0xff) << 8L)
				+ ((long) (bs[7] & 0xff) << 0L);

		assertEquals(value1, value2);
	}
}
