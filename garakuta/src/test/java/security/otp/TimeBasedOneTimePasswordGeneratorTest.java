package security.otp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TimeBasedOneTimePasswordGeneratorTest {

    @ParameterizedTest
    @MethodSource("parameters")
    void test(final Fixture fixture) throws Exception {
        final TimeBasedOneTimePasswordGenerator generator = TimeBasedOneTimePasswordGenerator
                .builder()
                .algorithm(fixture.algorithm)
                .digit(fixture.digit)
                .build();

        final int otp = generator.generate(fixture.key, fixture.unixTime);

        assertEquals(fixture.expected, otp);
    }

    static Stream<Fixture> parameters() {
        final byte[] key1 = "12345678901234567890".getBytes();
        final byte[] key2 = "12345678901234567890123456789012".getBytes();
        final byte[] key3 = "1234567890123456789012345678901234567890123456789012345678901234"
                .getBytes();

        return Stream.of(
                new Fixture("HmacSHA1", 8, key1, 59L, 94287082),
                new Fixture("HmacSHA256", 8, key2, 59L, 46119246),
                new Fixture("HmacSHA512", 8, key3, 59L, 90693936),
                new Fixture("HmacSHA1", 8, key1, 1111111109L, 7081804),
                new Fixture("HmacSHA256", 8, key2, 1111111109L, 68084774),
                new Fixture("HmacSHA512", 8, key3, 1111111109L, 25091201),
                new Fixture("HmacSHA1", 8, key1, 1111111111L, 14050471),
                new Fixture("HmacSHA256", 8, key2, 1111111111L, 67062674),
                new Fixture("HmacSHA512", 8, key3, 1111111111L, 99943326),
                new Fixture("HmacSHA1", 8, key1, 1234567890L, 89005924),
                new Fixture("HmacSHA256", 8, key2, 1234567890L, 91819424),
                new Fixture("HmacSHA512", 8, key3, 1234567890L, 93441116),
                new Fixture("HmacSHA1", 8, key1, 2000000000L, 69279037),
                new Fixture("HmacSHA256", 8, key2, 2000000000L, 90698825),
                new Fixture("HmacSHA512", 8, key3, 2000000000L, 38618901),
                new Fixture("HmacSHA1", 8, key1, 20000000000L, 65353130),
                new Fixture("HmacSHA256", 8, key2, 20000000000L, 77737706),
                new Fixture("HmacSHA512", 8, key3, 20000000000L, 47863826));
    }

    static class Fixture {
        String algorithm;
        int digit;
        byte[] key;
        long unixTime;
        int expected;

        public Fixture(final String algorithm, final int digit, final byte[] key,
                final long unixTime, final int expected) {
            this.algorithm = algorithm;
            this.digit = digit;
            this.key = key;
            this.unixTime = unixTime;
            this.expected = expected;
        }
    }
}
