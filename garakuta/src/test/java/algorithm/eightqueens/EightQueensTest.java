package algorithm.eightqueens;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EightQueensTest {

    @ParameterizedTest
    @MethodSource("pow")
    void pow(final int a, final int b, final int expected) {
        final int actual = EightQueens.pow(a, b);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> pow() {
        return Stream.of(
                Arguments.of(3, 0, 1),
                Arguments.of(3, 1, 3),
                Arguments.of(3, 2, 3 * 3),
                Arguments.of(3, 3, 3 * 3 * 3));
    }

    @ParameterizedTest
    @MethodSource("validate")
    void validate(final int[] queens, final boolean expected) {
        final boolean actual = new EightQueens(queens.length).validate(queens);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> validate() {
        return Stream.of(
                Arguments.of(new int[] { 0, 0, 0, 0 }, false),
                Arguments.of(new int[] { 0, 3, 1, 0 }, false),
                Arguments.of(new int[] { 0, 1, 2, 3 }, false),
                Arguments.of(new int[] { 0, 3, 1, 2 }, false),
                Arguments.of(new int[] { 2, 0, 3, 1 }, true));
    }
}
