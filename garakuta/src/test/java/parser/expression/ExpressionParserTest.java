package parser.expression;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static parser.expression.ExpressionNode.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExpressionParserTest {

    @ParameterizedTest
    @MethodSource("fixtures")
    void test(final String input, final ExpressionNode expectedNode, final int expectedValue) {
        final ExpressionNode node = new ExpressionParser(input).parse();
        final ExpressionEvaluator evaluator = new ExpressionEvaluator();
        assertAll(() -> {
            assertEquals(expectedNode, node);
            assertEquals(expectedValue, evaluator.evaluate(node));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "12x", "+1" })
    void parseException(final String input) {
        assertThrows(ParseException.class, new ExpressionParser(input)::parse);
    }

    static Stream<Arguments> fixtures() {
        return Stream.of(
                arguments("0", val(0), 0),
                arguments("1", val(1), 1),
                arguments("234567890", val(234567890), 234567890),
                arguments("1+2+3+4", add(add(add(val(1), val(2)), val(3)), val(4)),
                        1 + 2 + 3 + 4),
                arguments("9-1-2-3", sub(sub(sub(val(9), val(1)), val(2)), val(3)),
                        9 - 1 - 2 - 3),
                arguments("1*2*3*4", mul(mul(mul(val(1), val(2)), val(3)), val(4)),
                        1 * 2 * 3 * 4),
                arguments("210/5/3/2", div(div(div(val(210), val(5)), val(3)), val(2)),
                        210 / 5 / 3 / 2),
                arguments("1+2*3+4", add(add(val(1), mul(val(2), val(3))), val(4)),
                        1 + 2 * 3 + 4),
                arguments("(1+2)*(3+4)", mul(add(val(1), val(2)), add(val(3), val(4))),
                        (1 + 2) * (3 + 4)));
    }
}
