package parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import parser.Calc.ParseException;

@RunWith(Enclosed.class)
public class CalcTest {

    @RunWith(Parameterized.class)
    public static class NormalTest {

        @Parameter(0)
        public Fixture fixture;

        @Test
        public void test() throws Exception {
            final int actual = Calc.calc(fixture.source);
            final int expected = fixture.expected;
            assertThat(actual, is(expected));
        }

        @Parameters(name = "{0}")
        public static Iterable<Fixture> parameters() {
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
            return fs;
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

    @RunWith(Parameterized.class)
    public static class ParseErrorTest {
        @Rule
        public ExpectedException ee = ExpectedException.none();
        @Parameter(0)
        public String source;

        @Test
        public void test() throws Exception {
            ee.expect(ParseException.class);
            Calc.calc(source);
        }

        @Parameters(name = "{0}")
        public static Iterable<String> parameters() {
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
            return sources;
        }
    }
}
