package security.otp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TimeBasedOneTimePasswordGeneratorTest {

    @Parameter(0)
    public Fixture fixture;

    @Test
    public void test() throws Exception {
        TimeBasedOneTimePasswordGenerator generator = TimeBasedOneTimePasswordGenerator
                .builder()
                .algorithm(fixture.algorithm)
                .digit(fixture.digit)
                .build();

        int otp = generator.generate(fixture.key, fixture.unixTime);

        assertEquals(fixture.expected, otp);
    }

    @Parameters
    public static List<Fixture> parameters() {
        byte[] key1 = "12345678901234567890".getBytes();
        byte[] key2 = "12345678901234567890123456789012".getBytes();
        byte[] key3 = "1234567890123456789012345678901234567890123456789012345678901234".getBytes();

        List<Fixture> ps = new ArrayList<>();

        ps.add(new Fixture("HmacSHA1", 8, key1, 59L, 94287082));
        ps.add(new Fixture("HmacSHA256", 8, key2, 59L, 46119246));
        ps.add(new Fixture("HmacSHA512", 8, key3, 59L, 90693936));
        ps.add(new Fixture("HmacSHA1", 8, key1, 1111111109L, 7081804));
        ps.add(new Fixture("HmacSHA256", 8, key2, 1111111109L, 68084774));
        ps.add(new Fixture("HmacSHA512", 8, key3, 1111111109L, 25091201));
        ps.add(new Fixture("HmacSHA1", 8, key1, 1111111111L, 14050471));
        ps.add(new Fixture("HmacSHA256", 8, key2, 1111111111L, 67062674));
        ps.add(new Fixture("HmacSHA512", 8, key3, 1111111111L, 99943326));
        ps.add(new Fixture("HmacSHA1", 8, key1, 1234567890L, 89005924));
        ps.add(new Fixture("HmacSHA256", 8, key2, 1234567890L, 91819424));
        ps.add(new Fixture("HmacSHA512", 8, key3, 1234567890L, 93441116));
        ps.add(new Fixture("HmacSHA1", 8, key1, 2000000000L, 69279037));
        ps.add(new Fixture("HmacSHA256", 8, key2, 2000000000L, 90698825));
        ps.add(new Fixture("HmacSHA512", 8, key3, 2000000000L, 38618901));
        ps.add(new Fixture("HmacSHA1", 8, key1, 20000000000L, 65353130));
        ps.add(new Fixture("HmacSHA256", 8, key2, 20000000000L, 77737706));
        ps.add(new Fixture("HmacSHA512", 8, key3, 20000000000L, 47863826));

        return ps;
    }

    static class Fixture {
        String algorithm;
        int digit;
        byte[] key;
        long unixTime;
        int expected;
        public Fixture(String algorithm, int digit, byte[] key, long unixTime, int expected) {
            this.algorithm = algorithm;
            this.digit = digit;
            this.key = key;
            this.unixTime = unixTime;
            this.expected = expected;
        }
    }
}
