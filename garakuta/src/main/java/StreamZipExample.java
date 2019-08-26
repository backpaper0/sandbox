import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import misc.DelegateStream;

/**
 * Stream APIでScalaのList.zipみたいな事をしたい。
 * 
 * <pre>{@code
 * scala> (1 to 5).toList
 * res0: List[Int] = List(1, 2, 3, 4, 5)
 * 
 * scala> List("foo", "bar", "baz")
 * res1: List[String] = List(foo, bar, baz)
 * 
 * scala> res0.zip(res1)
 * res2: List[(Int, String)] = List((1,foo), (2,bar), (3,baz))
 * }</pre>
 * 
 */
public class StreamZipExample {

    public static void main(final String[] args) {

        final Stream<Integer> first = Stream.of(1, 2, 3, 4, 5);

        final Stream<String> second = Stream.of("foo", "bar", "baz");

        final String result = zip(first, second)
                //Pair.getFirstが奇数だけに絞る
                .filter((f, s) -> f % 2 == 1)
                //文字列にする
                .map((f, s) -> String.format("(%s, %s)", f, s))
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(result);
    }

    public static <T, U> ZippedStream<T, U> zip(final Stream<T> first,
            final Stream<U> second) {
        final Iterator<T> it1 = Spliterators.iterator(first.spliterator());
        final Iterator<U> it2 = Spliterators.iterator(second.spliterator());
        final Spliterator<Pair<T, U>> spliterator = new AbstractSpliterator<>(
                Long.MAX_VALUE, 0) {

            @Override
            public boolean tryAdvance(final Consumer<? super Pair<T, U>> action) {
                if (it1.hasNext() && it2.hasNext()) {
                    final T value1 = it1.next();
                    final U value2 = it2.next();
                    action.accept(new Pair<>(value1, value2));
                    return true;
                }
                return false;
            }
        };
        final Stream<Pair<T, U>> stream = StreamSupport.stream(spliterator, false);
        return new ZippedStreamImpl<>(stream);
    }

    public static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(final T first, final U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }

    public interface ZippedStream<T, U> extends Stream<Pair<T, U>> {

        <R> Stream<R> map(BiFunction<T, U, ? extends R> mapper);

        <R> Stream<R> flatMap(
                BiFunction<T, U, ? extends Stream<? extends R>> mapper);

        ZippedStream<T, U> filter(BiPredicate<T, U> predicate);
    }

    private static class ZippedStreamImpl<T, U>
            extends DelegateStream<Pair<T, U>> implements ZippedStream<T, U> {

        public ZippedStreamImpl(final Stream<Pair<T, U>> stream) {
            super(stream);
        }

        @Override
        public <R> Stream<R> map(final BiFunction<T, U, ? extends R> mapper) {
            return stream.map(a -> mapper.apply(a.getFirst(), a.getSecond()));
        }

        @Override
        public <R> Stream<R> flatMap(
                final BiFunction<T, U, ? extends Stream<? extends R>> mapper) {
            return stream
                    .flatMap(a -> mapper.apply(a.getFirst(), a.getSecond()));
        }

        @Override
        public ZippedStream<T, U> filter(final BiPredicate<T, U> predicate) {
            return new ZippedStreamImpl<>(stream
                    .filter(a -> predicate.test(a.getFirst(), a.getSecond())));
        }
    }
}
