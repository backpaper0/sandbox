package security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SHA1Test {

	@Test
	void test_hash_one_block_message() throws Exception {
		final byte[] src = "abc".getBytes();
		final byte[] actual = SHA1.hash(src);
		final byte[] expected = bytes(0xa9993e36, 0x4706816a, 0xba3e2571, 0x7850c26c, 0x9cd0d89d);
		assertArrayEquals(expected, actual);
	}

	@Test
	void test_hash_multi_block_message() throws Exception {
		final byte[] src = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes();
		final byte[] actual = SHA1.hash(src);
		final byte[] expected = bytes(0x84983e44, 0x1c3bd26e, 0xbaae4aa1, 0xf95129e5, 0xe54670f1);
		assertArrayEquals(expected, actual);
	}

	@Test
	void test_hash_long_message() throws Exception {
		final byte[] src = new byte[1_000_000];
		Arrays.fill(src, (byte) 'a');
		final byte[] actual = SHA1.hash(src);
		final byte[] expected = bytes(0x34aa973c, 0xd4c4daa4, 0xf61eeb2b, 0xdbad2731, 0x6534016f);
		assertArrayEquals(expected, actual);
	}

	private static byte[] bytes(final int... is) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (final int i : is) {
			for (int j = 0; j < 4; j++) {
				out.write((i >>> (24 - j * 8)) & 0xff);
			}
		}
		return out.toByteArray();
	}
}
