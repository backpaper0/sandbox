package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import parser.Calc.ParseException;

public class CalcTest {

    public static class NormalTest {

        @ParameterizedTest
        @MethodSource("parameters")
        public void test(final Fixture fixture) throws Exception {
            final int actual = Calc.calc(fixture.source);
            final int expected = fixture.expected;
            assertEquals(expected, actual);
        }

        public static Stream<Fixture> parameters() {
            final List<Fixture> fs = new ArrayList<>();
            fs.add(new Fixture("1 + 2 + 3", 1 + 2 + 3));
            fs.add(new Fixture("1 * 2 * 3", 1 * 2 * 3));
            fs.add(new Fixture("3 - 2 - 1", 3 - 2 - 1));
            fs.add(new Fixture("12 / 3 / 2", 12 / 3 / 2));
            fs.add(new Fixture("1 + 2 - (3 * 4 + 5) - 6 / (7 + 8 - 9)", 1 + 2
                    - (3 * 4 + 5) - 6 / (7 + 8 - 9)));
            fs.add(new Fixture("123", 123));
            fs.add(new Fixture("-123", -123));
            fs.add(new Fixture("1 - -123", 1 - -123));
            fs.add(new Fixture("+123", +123));
            fs.add(new Fixture("1 + +123", 1 + +123));
            fs.add(new Fixture("1 + +    123", 1 + +/**/123));
            fs.add(new Fixture("2 * -    123", 2 * -/**/123));
            return fs.stream();
        }

        static class Fixture {
            final String source;
            final int expected;

            public Fixture(final String source, final int expected) {
                super();
                this.source = source;
                this.expected = expected;
            }

            @Override
            public String toString() {
                return String.format("%s = %s", source, expected);
            }
        }
    }

    public static class ParseErrorTest {

        @ParameterizedTest
        @MethodSource("parameters")
        public void test(final String source) throws Exception {
            assertThrows(ParseException.class, () -> Calc.calc(source));
        }

        public static Stream<String> parameters() {
            final List<String> sources = new ArrayList<>();
            sources.add("+");
            sources.add("-");
            sources.add("*");
            sources.add("/");
            sources.add("*1");
            sources.add("/1");
            sources.add("1+");
            sources.add("1-");
            sources.add("1*");
            sources.add("1/");
            return sources.stream();
        }
    }
}
