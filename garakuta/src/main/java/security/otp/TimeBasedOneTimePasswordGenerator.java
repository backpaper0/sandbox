package security.otp;

/**
 * TOTP: Time-Based One-Time Password Algorithm
 * 
 * @see https://tools.ietf.org/html/rfc6238
 * 
 */
public final class TimeBasedOneTimePasswordGenerator {

    private final HmacBasedOneTimePasswordGenerator generator;
    private final long x;
    private final long t0;

    private TimeBasedOneTimePasswordGenerator(final Builder builder) {
        this.generator = HmacBasedOneTimePasswordGenerator
                .builder()
                .algorithm(builder.algorithm)
                .digit(builder.digit)
                .build();
        this.x = builder.x;
        this.t0 = builder.t0;
    }

    public int generate(final byte[] key) {
        return generate(key, System.currentTimeMillis() / 1000L);
    }

    public int generate(final byte[] key, final long unixTime) {
        long t = (unixTime - t0) / x;
        final byte[] value = new byte[8];
        for (int i = value.length - 1; i >= 0; i--) {
            value[i] = (byte) (t & 0xffL);
            t >>= 8L;
        }
        return generator.generate(key, value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String algorithm = "HmacSHA1";
        private int digit = 6;
        private long x = 30;
        private long t0 = 0;

        private Builder() {
        }

        public Builder algorithm(final String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder digit(final int digit) {
            this.digit = digit;
            return this;
        }

        public Builder x(final long x) {
            this.x = x;
            return this;
        }

        public Builder t0(final long t0) {
            this.t0 = t0;
            return this;
        }

        public TimeBasedOneTimePasswordGenerator build() {
            return new TimeBasedOneTimePasswordGenerator(this);
        }
    }
}
