package security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SHA1 {

    public static byte[] hash(final byte[] src) {
        return hash(new ByteArrayInputStream(src));
    }

    public static byte[] hash(final InputStream in) {
        final Blocks blocks = new Blocks(64, 8, in);
        final int[] hash = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0 };
        for (final byte[] bs : blocks) {
            final int[] w = new int[80];
            for (int t = 0; t < 16; t++) {
                w[t] = ((bs[t * 4] & 0xff) << 24)
                        | ((bs[t * 4 + 1] & 0xff) << 16)
                        | ((bs[t * 4 + 2] & 0xff) << 8)
                        | (bs[t * 4 + 3] & 0xff);
            }
            for (int t = 16; t < 80; t++) {
                w[t] = rotl(1, w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16]);
            }
            int a = hash[0];
            int b = hash[1];
            int c = hash[2];
            int d = hash[3];
            int e = hash[4];
            for (int t = 0; t < 80; t++) {
                final int temp = rotl(5, a) + f(t, b, c, d) + e + k(t) + w[t];
                e = d;
                d = c;
                c = rotl(30, b);
                b = a;
                a = temp;
            }
            hash[0] = a + hash[0];
            hash[1] = b + hash[1];
            hash[2] = c + hash[2];
            hash[3] = d + hash[3];
            hash[4] = e + hash[4];
        }
        final byte[] dest = new byte[20];
        for (int i = 0; i < 5; i++) {
            dest[i * 4 + 0] = (byte) (hash[i] >>> 24);
            dest[i * 4 + 1] = (byte) (hash[i] >>> 16);
            dest[i * 4 + 2] = (byte) (hash[i] >>> 8);
            dest[i * 4 + 3] = (byte) hash[i];
        }
        return dest;
    }

    private static int rotl(final int n, final int x) {
        return (x << n) | (x >>> (32 - n));
    }

    private static int f(final int t, final int x, final int y, final int z) {
        if (t < 20) {
            return (x & y) ^ (~x & z);
        } else if (t < 40) {
            return x ^ y ^ z;
        } else if (t < 60) {
            return (x & y) ^ (x & z) ^ (y & z);
        }
        return x ^ y ^ z;
    }

    private static int k(final int t) {
        if (t < 20) {
            return 0x5a827999;
        } else if (t < 40) {
            return 0x6ed9eba1;
        } else if (t < 60) {
            return 0x8f1bbcdc;
        }
        return 0xca62c1d6;
    }
}
