import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class LambdaExpression {

    public static class OptionalOptional2Optional {

        private final Optional<Optional<Integer>> src = Optional.of(Optional
                .of(123));

        @Test
        public void example1() throws Exception {
            Optional<Integer> dest = src.flatMap(Function.identity());
            assertThat(dest.isPresent(), is(true));
            assertThat(dest.get(), is(123));
        }
    }

    public static class OptionalList2List {

        private final List<Optional<Integer>> src = Arrays.asList(
                Optional.of(1), Optional.empty(), Optional.of(2),
                Optional.empty(), Optional.of(3));

        @Test
        public void example1() throws Exception {
            List<Integer> dest = src
                    .stream()
                    .flatMap(
                            opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                    .collect(Collectors.toList());
            assertThat(dest, is(Arrays.asList(1, 2, 3)));
        }

        @Test
        public void example2() throws Exception {
            List<Integer> dest = src.stream().filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toList());
            assertThat(dest, is(Arrays.asList(1, 2, 3)));
        }
    }
}
