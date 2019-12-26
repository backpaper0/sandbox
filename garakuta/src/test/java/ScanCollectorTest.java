import static org.junit.jupiter.api.Assertions.*;

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

import org.junit.jupiter.api.Test;

/**
 * @see http://backpaper0.github.io/2014/10/04/stream_collect.html
 */
public class ScanCollectorTest {

    @Test
    public void test() throws Exception {
        final Stream<Integer> xs = IntStream.rangeClosed(1, 5).boxed();
        final List<Integer> actual = xs.collect(scan(0, Integer::sum));
        assertEquals(Arrays.asList(0, 1, 3, 6, 10, 15), actual);
    }

    @Test
    public void testEmptyStream() throws Exception {
        final Stream<Integer> xs = Stream.empty();
        final List<Integer> actual = xs.collect(scan(0, Integer::sum));
        assertEquals(Arrays.asList(0), actual);
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
    public static <T, R> Collector<T, ?, List<R>> scan(final R init,
            final BiFunction<R, T, R> fn) {

        final Supplier<LinkedList<R>> supplier = () -> new LinkedList<>(
                Collections.singletonList(init));

        final BiConsumer<LinkedList<R>, T> accumulator = (acc, t) -> acc.add(fn
                .apply(acc.getLast(), t));

        final BinaryOperator<LinkedList<R>> combiner = (acc1, acc2) -> {
            acc1.addAll(acc2);
            return acc1;
        };

        final Function<LinkedList<R>, List<R>> finisher = acc -> acc;

        final Characteristics characteristics = Characteristics.IDENTITY_FINISH;

        return Collector.of(supplier, accumulator, combiner, finisher,
                characteristics);
    }
}
