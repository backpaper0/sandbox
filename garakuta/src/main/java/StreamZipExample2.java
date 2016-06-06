import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamZipExample2 {

    public static void main(String[] args) {
        Stream<String> s1 = Arrays.stream("abcdefg".split(""));
        Stream<Integer> s2 = IntStream.range(1, 10).boxed();
        zip(s1, s2, (t, u) -> t + u).forEach(System.out::println);
    }

    static <T, U, V> Stream<V> zip(Stream<T> s1, Stream<U> s2, BiFunction<T, U, V> f) {
        Spliterator<T> sp1 = s1.spliterator();
        Spliterator<U> sp2 = s2.spliterator();
        long est = Long.min(sp1.estimateSize(), sp2.estimateSize());
        int additionalCharacteristics = sp1.characteristics() & sp2.characteristics();
        Spliterator<V> spliterator = new AbstractSpliterator<V>(est, additionalCharacteristics) {

            @Override
            public boolean tryAdvance(Consumer<? super V> action) {
                return sp1.tryAdvance(t -> sp2.tryAdvance(u -> action.accept(f.apply(t, u))));
            }
        };
        return StreamSupport.stream(spliterator, false);
    }
}
