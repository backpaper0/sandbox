import java.util.Arrays;

public class Sha1 {

    public static byte[] hash(byte[] src) {
        byte[] bs = padding(src);

        int[] hash = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476,
                0xc3d2e1f0 };

        for (int i = 0; i < bs.length / 64; i++) {

            int[] w = new int[80];
            for (int t = 0; t < 16; t++) {
                int base = i * 64 + t * 4;
                w[t] = ((bs[base] & 0xff) << 24)
                        | ((bs[base + 1] & 0xff) << 16)
                        | ((bs[base + 2] & 0xff) << 8) | (bs[base + 3] & 0xff);
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
                int temp = rotl(5, a) + f(t, b, c, d) + e + k(t) + w[t];
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

        byte[] dest = new byte[20];
        for (int i = 0; i < 5; i++) {
            int base = i * 4;
            dest[base] = (byte) (hash[i] >> 24);
            dest[base + 1] = (byte) (hash[i] >> 16);
            dest[base + 2] = (byte) (hash[i] >> 8);
            dest[base + 3] = (byte) hash[i];
        }
        return dest;
    }

    private static byte[] padding(byte[] src) {
        int padLength = 64 - (src.length % 64);
        if (padLength < 9) {
            padLength += 64;
        }
        byte[] bs = Arrays.copyOf(src, src.length + padLength);
        bs[src.length] = (byte) 0x80;
        bs[bs.length - 4] = (byte) (src.length * 8 >> 24);
        bs[bs.length - 3] = (byte) (src.length * 8 >> 16);
        bs[bs.length - 2] = (byte) (src.length * 8 >> 8);
        bs[bs.length - 1] = (byte) (src.length * 8);
        return bs;
    }

    private static int rotl(int n, int x) {
        return (x << n) | (x >>> (32 - n));
    }

    private static int f(int t, int x, int y, int z) {
        if (t < 20) {
            return (x & y) ^ (~x & z);
        } else if (t < 40) {
            return x ^ y ^ z;
        } else if (t < 60) {
            return (x & y) ^ (x & z) ^ (y & z);
        }
        return x ^ y ^ z;
    }

    private static int k(int t) {
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