package parser.expression;

import static org.junit.jupiter.api.Assertions.*;
import static parser.expression.ExpressionNode.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExpressionParserTest {

    @ParameterizedTest
    @MethodSource("fixtures")
    void test(final Fixture fixture) {
        final ExpressionNode node = new ExpressionParser(fixture.input).parse();
        final ExpressionEvaluator evaluator = new ExpressionEvaluator();
        assertAll(() -> {
            assertEquals(fixture.expectedNode, node);
            assertEquals(fixture.expectedValue, evaluator.evaluate(node));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "12x", "+1" })
    void parseException(final String input) {
        assertThrows(ParseException.class, new ExpressionParser(input)::parse);
    }

    static Stream<Fixture> fixtures() {
        return Stream.of(
                new Fixture("0", val(0), 0),
                new Fixture("1", val(1), 1),
                new Fixture("234567890", val(234567890), 234567890),
                new Fixture("1+2*3+4", add(add(val(1), mul(val(2), val(3))), val(4)),
                        1 + 2 * 3 + 4),
                new Fixture("(1+2)*(3+4)", mul(add(val(1), val(2)), add(val(3), val(4))),
                        (1 + 2) * (3 + 4)));
    }

    static class Fixture {

        final String input;
        final ExpressionNode expectedNode;
        final int expectedValue;

        Fixture(final String input, final ExpressionNode expectedNode, final int expectedValue) {
            this.input = input;
            this.expectedNode = expectedNode;
            this.expectedValue = expectedValue;
        }

        @Override
        public String toString() {
            return input;
        }
    }
}
