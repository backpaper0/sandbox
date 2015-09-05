import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static void main(String[] args) {

        Stream<Integer> first = Stream.of(1, 2, 3, 4, 5);

        Stream<String> second = Stream.of("foo", "bar", "baz");

        String result = zip(first, second)
                .map(p -> String.format("(%s, %s)", p.getFirst(),
                        p.getSecond()))
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(result);
    }

    public static <T, U> Stream<Pair<T, U>> zip(Stream<T> first,
            Stream<U> second) {
        Iterator<T> it1 = Spliterators.iterator(first.spliterator());
        Iterator<U> it2 = Spliterators.iterator(second.spliterator());
        Spliterator<Pair<T, U>> spliterator = new AbstractSpliterator<Pair<T, U>>(
                Long.MAX_VALUE, 0) {

            @Override
            public boolean tryAdvance(Consumer<? super Pair<T, U>> action) {
                if (it1.hasNext() && it2.hasNext()) {
                    T value1 = it1.next();
                    U value2 = it2.next();
                    action.accept(new Pair<>(value1, value2));
                    return true;
                }
                return false;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    public static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
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
}
