
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class LambdaExpression {

    public static class OptionalOptional2Optional {

        private final Optional<Optional<Integer>> src = Optional.of(Optional
                .of(123));

        @Test
        public void example1() throws Exception {
            final Optional<Integer> dest = src.flatMap(Function.identity());
            assertTrue(dest.isPresent());
            assertEquals(123, dest.get());
        }
    }

    public static class OptionalList2List {

        private final List<Optional<Integer>> src = Arrays.asList(
                Optional.of(1), Optional.empty(), Optional.of(2),
                Optional.empty(), Optional.of(3));

        @Test
        public void example1() throws Exception {
            final List<Integer> dest = src
                    .stream()
                    .flatMap(
                            opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                    .collect(Collectors.toList());
            assertEquals(Arrays.asList(1, 2, 3), dest);
        }

        @Test
        public void example2() throws Exception {
            final List<Integer> dest = src.stream().filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toList());
            assertEquals(Arrays.asList(1, 2, 3), dest);
        }
    }
}
