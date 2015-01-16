package security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Test;

public class SHA1Test {

    @Test
    public void test_hash_one_block_message() throws Exception {
        byte[] src = "abc".getBytes();
        byte[] actual = SHA1.hash(src);
        byte[] expected = bytes(0xa9993e36, 0x4706816a, 0xba3e2571, 0x7850c26c, 0x9cd0d89d);
        assertThat(actual, is(expected));
    }

    @Test
    public void test_hash_multi_block_message() throws Exception {
        byte[] src = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes();
        byte[] actual = SHA1.hash(src);
        byte[] expected = bytes(0x84983e44, 0x1c3bd26e, 0xbaae4aa1, 0xf95129e5, 0xe54670f1);
        assertThat(actual, is(expected));
    }

    @Test
    public void test_hash_long_message() throws Exception {
        byte[] src = new byte[1_000_000];
        Arrays.fill(src, (byte) 'a');
        byte[] actual = SHA1.hash(src);
        byte[] expected = bytes(0x34aa973c, 0xd4c4daa4, 0xf61eeb2b, 0xdbad2731, 0x6534016f);
        assertThat(actual, is(expected));
    }

    private static byte[] bytes(int... is) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i : is) {
            for (int j = 0; j < 4; j++) {
                out.write((i >>> (24 - j * 8)) & 0xff);
            }
        }
        return out.toByteArray();
    }
}
