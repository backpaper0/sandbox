package security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SHA256 {

    private static final int[] K = {
        0x428a2f98,
        0x71374491,
        0xb5c0fbcf,
        0xe9b5dba5,
        0x3956c25b,
        0x59f111f1,
        0x923f82a4,
        0xab1c5ed5,
        0xd807aa98,
        0x12835b01,
        0x243185be,
        0x550c7dc3,
        0x72be5d74,
        0x80deb1fe,
        0x9bdc06a7,
        0xc19bf174,
        0xe49b69c1,
        0xefbe4786,
        0x0fc19dc6,
        0x240ca1cc,
        0x2de92c6f,
        0x4a7484aa,
        0x5cb0a9dc,
        0x76f988da,
        0x983e5152,
        0xa831c66d,
        0xb00327c8,
        0xbf597fc7,
        0xc6e00bf3,
        0xd5a79147,
        0x06ca6351,
        0x14292967,
        0x27b70a85,
        0x2e1b2138,
        0x4d2c6dfc,
        0x53380d13,
        0x650a7354,
        0x766a0abb,
        0x81c2c92e,
        0x92722c85,
        0xa2bfe8a1,
        0xa81a664b,
        0xc24b8b70,
        0xc76c51a3,
        0xd192e819,
        0xd6990624,
        0xf40e3585,
        0x106aa070,
        0x19a4c116,
        0x1e376c08,
        0x2748774c,
        0x34b0bcb5,
        0x391c0cb3,
        0x4ed8aa4a,
        0x5b9cca4f,
        0x682e6ff3,
        0x748f82ee,
        0x78a5636f,
        0x84c87814,
        0x8cc70208,
        0x90befffa,
        0xa4506ceb,
        0xbef9a3f7,
        0xc67178f2 };

    public static byte[] hash(byte[] src) {
        return hash(new ByteArrayInputStream(src));
    }

    public static byte[] hash(InputStream in) {
        Blocks blocks = new Blocks(64, 8, in);
        int[] hash =
            {
                0x6a09e667,
                0xbb67ae85,
                0x3c6ef372,
                0xa54ff53a,
                0x510e527f,
                0x9b05688c,
                0x1f83d9ab,
                0x5be0cd19 };
        for (byte[] bs : blocks) {
            int[] w = new int[64];
            for (int t = 0; t < 16; t++) {
                w[t] =
                    ((bs[t * 4] & 0xff) << 24)
                        | ((bs[t * 4 + 1] & 0xff) << 16)
                        | ((bs[t * 4 + 2] & 0xff) << 8)
                        | (bs[t * 4 + 3] & 0xff);
            }
            for (int t = 16; t < 64; t++) {
                w[t] = lowerSigma1(w[t - 2]) + w[t - 7] + lowerSigma0(w[t - 15]) + w[t - 16];
            }
            int a = hash[0];
            int b = hash[1];
            int c = hash[2];
            int d = hash[3];
            int e = hash[4];
            int f = hash[5];
            int g = hash[6];
            int h = hash[7];
            for (int t = 0; t < 64; t++) {
                int temp1 = h + upperSigma1(e) + ch(e, f, g) + K[t] + w[t];
                int temp2 = upperSigma0(a) + maj(a, b, c);
                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
            hash[0] = a + hash[0];
            hash[1] = b + hash[1];
            hash[2] = c + hash[2];
            hash[3] = d + hash[3];
            hash[4] = e + hash[4];
            hash[5] = f + hash[5];
            hash[6] = g + hash[6];
            hash[7] = h + hash[7];
        }
        byte[] dest = new byte[32];
        for (int i = 0; i < 8; i++) {
            dest[i * 4 + 0] = (byte) (hash[i] >>> 24);
            dest[i * 4 + 1] = (byte) (hash[i] >>> 16);
            dest[i * 4 + 2] = (byte) (hash[i] >>> 8);
            dest[i * 4 + 3] = (byte) hash[i];
        }
        return dest;
    }

    private static int ch(int x, int y, int z) {
        return (x & y) ^ (~x & z);
    }

    private static int maj(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private static int rotr(int n, int x) {
        return (x >>> n) | (x << (32 - n));
    }

    private static int upperSigma0(int x) {
        return rotr(2, x) ^ rotr(13, x) ^ rotr(22, x);
    }

    private static int upperSigma1(int x) {
        return rotr(6, x) ^ rotr(11, x) ^ rotr(25, x);
    }

    private static int lowerSigma0(int x) {
        return rotr(7, x) ^ rotr(18, x) ^ (x >>> 3);
    }

    private static int lowerSigma1(int x) {
        return rotr(17, x) ^ rotr(19, x) ^ (x >>> 10);
    }
}
