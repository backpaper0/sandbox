package security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SHA384 {

    private static final long[] K = {
        0x428a2f98d728ae22L,
        0x7137449123ef65cdL,
        0xb5c0fbcfec4d3b2fL,
        0xe9b5dba58189dbbcL,
        0x3956c25bf348b538L,
        0x59f111f1b605d019L,
        0x923f82a4af194f9bL,
        0xab1c5ed5da6d8118L,
        0xd807aa98a3030242L,
        0x12835b0145706fbeL,
        0x243185be4ee4b28cL,
        0x550c7dc3d5ffb4e2L,
        0x72be5d74f27b896fL,
        0x80deb1fe3b1696b1L,
        0x9bdc06a725c71235L,
        0xc19bf174cf692694L,
        0xe49b69c19ef14ad2L,
        0xefbe4786384f25e3L,
        0x0fc19dc68b8cd5b5L,
        0x240ca1cc77ac9c65L,
        0x2de92c6f592b0275L,
        0x4a7484aa6ea6e483L,
        0x5cb0a9dcbd41fbd4L,
        0x76f988da831153b5L,
        0x983e5152ee66dfabL,
        0xa831c66d2db43210L,
        0xb00327c898fb213fL,
        0xbf597fc7beef0ee4L,
        0xc6e00bf33da88fc2L,
        0xd5a79147930aa725L,
        0x06ca6351e003826fL,
        0x142929670a0e6e70L,
        0x27b70a8546d22ffcL,
        0x2e1b21385c26c926L,
        0x4d2c6dfc5ac42aedL,
        0x53380d139d95b3dfL,
        0x650a73548baf63deL,
        0x766a0abb3c77b2a8L,
        0x81c2c92e47edaee6L,
        0x92722c851482353bL,
        0xa2bfe8a14cf10364L,
        0xa81a664bbc423001L,
        0xc24b8b70d0f89791L,
        0xc76c51a30654be30L,
        0xd192e819d6ef5218L,
        0xd69906245565a910L,
        0xf40e35855771202aL,
        0x106aa07032bbd1b8L,
        0x19a4c116b8d2d0c8L,
        0x1e376c085141ab53L,
        0x2748774cdf8eeb99L,
        0x34b0bcb5e19b48a8L,
        0x391c0cb3c5c95a63L,
        0x4ed8aa4ae3418acbL,
        0x5b9cca4f7763e373L,
        0x682e6ff3d6b2b8a3L,
        0x748f82ee5defb2fcL,
        0x78a5636f43172f60L,
        0x84c87814a1f0ab72L,
        0x8cc702081a6439ecL,
        0x90befffa23631e28L,
        0xa4506cebde82bde9L,
        0xbef9a3f7b2c67915L,
        0xc67178f2e372532bL,
        0xca273eceea26619cL,
        0xd186b8c721c0c207L,
        0xeada7dd6cde0eb1eL,
        0xf57d4f7fee6ed178L,
        0x06f067aa72176fbaL,
        0x0a637dc5a2c898a6L,
        0x113f9804bef90daeL,
        0x1b710b35131c471bL,
        0x28db77f523047d84L,
        0x32caab7b40c72493L,
        0x3c9ebe0a15c9bebcL,
        0x431d67c49c100d4cL,
        0x4cc5d4becb3e42b6L,
        0x597f299cfc657e2aL,
        0x5fcb6fab3ad6faecL,
        0x6c44198c4a475817L };

    public static byte[] hash(final byte[] src) {
        return hash(new ByteArrayInputStream(src));
    }

    public static byte[] hash(final InputStream in) {
        final Blocks blocks = new Blocks(128, 16, in);
        final long[] hash =
            {
                0xcbbb9d5dc1059ed8L,
                0x629a292a367cd507L,
                0x9159015a3070dd17L,
                0x152fecd8f70e5939L,
                0x67332667ffc00b31L,
                0x8eb44a8768581511L,
                0xdb0c2e0d64f98fa7L,
                0x47b5481dbefa4fa4L };
        for (final byte[] bs : blocks) {
            final long[] w = new long[80];
            for (int t = 0; t < 16; t++) {
                w[t] =
                    ((bs[t * 8] & 0xffL) << 56L)
                        | ((bs[t * 8 + 1] & 0xffL) << 48L)
                        | ((bs[t * 8 + 2] & 0xffL) << 40L)
                        | ((bs[t * 8 + 3] & 0xffL) << 32L)
                        | ((bs[t * 8 + 4] & 0xffL) << 24L)
                        | ((bs[t * 8 + 5] & 0xffL) << 16L)
                        | ((bs[t * 8 + 6] & 0xffL) << 8L)
                        | (bs[t * 8 + 7] & 0xffL);
            }
            for (int t = 16; t < 80; t++) {
                w[t] = lowerSigma1(w[t - 2]) + w[t - 7] + lowerSigma0(w[t - 15]) + w[t - 16];
            }
            long a = hash[0];
            long b = hash[1];
            long c = hash[2];
            long d = hash[3];
            long e = hash[4];
            long f = hash[5];
            long g = hash[6];
            long h = hash[7];
            for (int t = 0; t < 80; t++) {
                final long temp1 = h + upperSigma1(e) + ch(e, f, g) + K[t] + w[t];
                final long temp2 = upperSigma0(a) + maj(a, b, c);
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
        final byte[] dest = new byte[48];
        for (int i = 0; i < 6; i++) {
            dest[i * 8 + 0] = (byte) (hash[i] >>> 56L);
            dest[i * 8 + 1] = (byte) (hash[i] >>> 48L);
            dest[i * 8 + 2] = (byte) (hash[i] >>> 40L);
            dest[i * 8 + 3] = (byte) (hash[i] >>> 32L);
            dest[i * 8 + 4] = (byte) (hash[i] >>> 24L);
            dest[i * 8 + 5] = (byte) (hash[i] >>> 16L);
            dest[i * 8 + 6] = (byte) (hash[i] >>> 8L);
            dest[i * 8 + 7] = (byte) hash[i];
        }
        return dest;
    }

    private static long ch(final long x, final long y, final long z) {
        return (x & y) ^ (~x & z);
    }

    private static long maj(final long x, final long y, final long z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private static long rotr(final long n, final long x) {
        return (x >>> n) | (x << (64L - n));
    }

    private static long upperSigma0(final long x) {
        return rotr(28L, x) ^ rotr(34L, x) ^ rotr(39L, x);
    }

    private static long upperSigma1(final long x) {
        return rotr(14L, x) ^ rotr(18L, x) ^ rotr(41L, x);
    }

    private static long lowerSigma0(final long x) {
        return rotr(1L, x) ^ rotr(8L, x) ^ (x >>> 7L);
    }

    private static long lowerSigma1(final long x) {
        return rotr(19L, x) ^ rotr(61L, x) ^ (x >>> 6L);
    }
}
