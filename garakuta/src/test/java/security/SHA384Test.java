package security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SHA384Test {

	@Test
	void test_hash_one_block_message() throws Exception {
		final byte[] src = "abc".getBytes();
		final byte[] actual = SHA384.hash(src);
		final byte[] expected = bytes(
				0xcb00753f45a35e8bL,
				0xb5a03d699ac65007L,
				0x272c32ab0eded163L,
				0x1a8b605a43ff5bedL,
				0x8086072ba1e7cc23L,
				0x58baeca134c825a7L);
		assertArrayEquals(expected, actual);
	}

	@Test
	void test_hash_multi_block_message() throws Exception {
		final byte[] src = "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"
				.getBytes();
		final byte[] actual = SHA384.hash(src);
		final byte[] expected = bytes(
				0x09330c33f71147e8L,
				0x3d192fc782cd1b47L,
				0x53111b173b3b05d2L,
				0x2fa08086e3b0f712L,
				0xfcc7c71a557e2db9L,
				0x66c3e9fa91746039L);
		assertArrayEquals(expected, actual);
	}

	@Test
	void test_hash_long_message() throws Exception {
		final byte[] src = new byte[1_000_000];
		Arrays.fill(src, (byte) 'a');
		final byte[] actual = SHA384.hash(src);
		final byte[] expected = bytes(
				0x9d0e1809716474cbL,
				0x086e834e310a4a1cL,
				0xed149e9c00f24852L,
				0x7972cec5704c2a5bL,
				0x07b8b3dc38ecc4ebL,
				0xae97ddd87f3d8985L);
		assertArrayEquals(expected, actual);
	}

	private static byte[] bytes(final long... ls) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (final long l : ls) {
			for (long j = 0L; j < 8L; j++) {
				out.write((int) ((l >>> (56L - j * 8L)) & 0xffL));
			}
		}
		return out.toByteArray();
	}
}
