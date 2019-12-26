package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import parser.Calc.ParseException;

class CalcTest {

    static class NormalTest {

        @ParameterizedTest
        @MethodSource("parameters")
        void test(final Fixture fixture) throws Exception {
            final int actual = Calc.calc(fixture.source);
            final int expected = fixture.expected;
            assertEquals(expected, actual);
        }

        static Stream<Fixture> parameters() {
            return Stream.of(
                    new Fixture("1 + 2 + 3", 1 + 2 + 3),
                    new Fixture("1 * 2 * 3", 1 * 2 * 3),
                    new Fixture("3 - 2 - 1", 3 - 2 - 1),
                    new Fixture("12 / 3 / 2", 12 / 3 / 2),
                    new Fixture("1 + 2 - (3 * 4 + 5) - 6 / (7 + 8 - 9)",
                            1 + 2 - (3 * 4 + 5) - 6 / (7 + 8 - 9)),
                    new Fixture("123", 123),
                    new Fixture("-123", -123),
                    new Fixture("1 - -123", 1 - -123),
                    new Fixture("+123", +123),
                    new Fixture("1 + +123", 1 + +123),
                    new Fixture("1 + +    123", 1 + +/**/123),
                    new Fixture("2 * -    123", 2 * -/**/123));
        }

        static class Fixture {
            final String source;
            final int expected;

            public Fixture(final String source, final int expected) {
                this.source = source;
                this.expected = expected;
            }

            @Override
            public String toString() {
                return String.format("%s = %s", source, expected);
            }
        }
    }

    static class ParseErrorTest {

        @ParameterizedTest
        @MethodSource("parameters")
        void test(final String source) throws Exception {
            assertThrows(ParseException.class, () -> Calc.calc(source));
        }

        static Stream<String> parameters() {
            return Stream.of(
                    "+",
                    "-",
                    "*",
                    "/",
                    "*1",
                    "/1",
                    "1+",
                    "1-",
                    "1*",
                    "1/");
        }
    }
}
