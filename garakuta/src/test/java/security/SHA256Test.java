package security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SHA256Test {

    @Test
    void test_hash_one_block_message() throws Exception {
        final byte[] src = "abc".getBytes();
        final byte[] actual = SHA256.hash(src);
        final byte[] expected = bytes(
                0xba7816bf,
                0x8f01cfea,
                0x414140de,
                0x5dae2223,
                0xb00361a3,
                0x96177a9c,
                0xb410ff61,
                0xf20015ad);
        assertArrayEquals(expected, actual);
    }

    @Test
    void test_hash_multi_block_message() throws Exception {
        final byte[] src = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes();
        final byte[] actual = SHA256.hash(src);
        final byte[] expected = bytes(
                0x248d6a61,
                0xd20638b8,
                0xe5c02693,
                0x0c3e6039,
                0xa33ce459,
                0x64ff2167,
                0xf6ecedd4,
                0x19db06c1);
        assertArrayEquals(expected, actual);
    }

    @Test
    void test_hash_long_message() throws Exception {
        final byte[] src = new byte[1_000_000];
        Arrays.fill(src, (byte) 'a');
        final byte[] actual = SHA256.hash(src);
        final byte[] expected = bytes(
                0xcdc76e5c,
                0x9914fb92,
                0x81a1c7e2,
                0x84d73e67,
                0xf1809a48,
                0xa497200e,
                0x046d39cc,
                0xc7112cd0);
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
