import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @see http://backpaper0.github.io/2014/10/04/stream_collect.html
 */
public class ScanCollectorTest {

    @Test
    public void test() throws Exception {
        Stream<Integer> xs = IntStream.rangeClosed(1, 5).boxed();
        List<Integer> actual = xs.collect(scan(0, Integer::sum));
        assertThat(actual, is(Arrays.asList(0, 1, 3, 6, 10, 15)));
    }

    @Test
    public void testEmptyStream() throws Exception {
        Stream<Integer> xs = Stream.empty();
        List<Integer> actual = xs.collect(scan(0, Integer::sum));
        assertThat(actual, is(Arrays.asList(0)));
    }

    /**
     * <tt>[t<sub>0</sub>, t<sub>1</sub>, t<sub>2</sub> ... t<sub>n</sub>]</tt>というストリームに対して
     * <pre>
     * r<sub>0</sub> = init
     * r<sub>1</sub> = fn(r<sub>0</sub>, t<sub>0</sub>)
     * r<sub>2</sub> = fn(r<sub>1</sub>, t<sub>1</sub>)
     * r<sub>3</sub> = fn(r<sub>2</sub>, t<sub>2</sub>)
     * .
     * .
     * .
     * r<sub>n+1</sub> = fn(r<sub>n</sub>, t<sub>n</sub>)
     * </pre>
     * と処理して<tt>[r<sub>0</sub>, r<sub>1</sub>, r<sub>2</sub> ... r<sub>n+1</sub>]</tt>というリストを返します。
     * 
     * @param init
     * @param fn
     * @return
     */
    public static <T, R> Collector<T, ?, List<R>> scan(R init,
            BiFunction<R, T, R> fn) {

        Supplier<LinkedList<R>> supplier = () -> new LinkedList<>(
                Collections.singletonList(init));

        BiConsumer<LinkedList<R>, T> accumulator = (acc, t) -> acc.add(fn
                .apply(acc.getLast(), t));

        BinaryOperator<LinkedList<R>> combiner = (acc1, acc2) -> {
            acc1.addAll(acc2);
            return acc1;
        };

        Function<LinkedList<R>, List<R>> finisher = acc -> acc;

        Characteristics characteristics = Characteristics.IDENTITY_FINISH;

        return Collector.of(supplier, accumulator, combiner, finisher,
                characteristics);
    }
}
