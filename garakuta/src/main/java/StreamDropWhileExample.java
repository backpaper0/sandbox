import static java.util.stream.Collectors.joining;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Stream APIでScalaのList.dropWhileのような事をしたいけどそんなメソッドは無い！
 * 
 * <pre>{@code
 * scala> (1 to 10).toList
 * res0: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
 * 
 * scala> res0.dropWhile(a => a != 6)
 * res1: List[Int] = List(6, 7, 8, 9, 10)
 * 
 * scala>
 * }</pre>
 *
 * なので足掻いてみる。
 * 
 */
public class StreamDropWhileExample {

    public static void main(String[] args) {

        //1,2,3,4,5...みたいな無限ストリームを用意する。
        Stream<Integer> original = Stream.iterate(1, a -> a + 1);

        //先頭から6でない値を飛ばす
        Stream<Integer> stream = dropWhile(original, a -> a != 6);

        //5つ取り出してカンマ区切りの文字列にする
        String result = stream.map(Objects::toString).limit(5)
                .collect(joining(", "));

        System.out.println(result);

        //takeWhiteも実装してみた。
        System.out.println(takeWhile(Stream.iterate(1, a -> a + 1), a -> a != 6)
                .map(Objects::toString).limit(5).collect(joining(", ")));
    }

    public static <T> Stream<T> dropWhile(Stream<T> stream,
            Predicate<T> predicate) {
        //predicate.testの結果付きの値にして
        //要らない分をfilterで取り除いて
        //元の値をmapで取り出す
        ElementSpliterator<T> sp = new ElementSpliterator<>(stream, predicate);
        return StreamSupport.stream(sp, false).filter(Element::isNecessary)
                .map(Element::get);
    }

    public static <T> Stream<T> takeWhile(Stream<T> stream,
            Predicate<T> predicate) {
        TakeWhileSpliterator<T> sp = new TakeWhileSpliterator<>(stream,
                predicate);
        return StreamSupport.stream(sp, false).filter(Element::isNecessary)
                .map(Element::get);
    }

    private static class Element<T> {
        private final boolean necessary;
        private final T value;

        public Element(boolean necessary, T value) {
            this.necessary = necessary;
            this.value = value;
        }

        public boolean isNecessary() {
            return necessary;
        }

        public T get() {
            return value;
        }
    }

    private static class ElementSpliterator<T>
            extends AbstractSpliterator<Element<T>> {

        private final Iterator<T> iterator;
        private final Predicate<T> predicate;
        private boolean necessary;

        public ElementSpliterator(Stream<T> stream, Predicate<T> predicate) {
            super(Long.MAX_VALUE, 0);
            this.iterator = Spliterators.iterator(stream.spliterator());
            this.predicate = predicate;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Element<T>> action) {
            if (iterator.hasNext()) {
                T value = iterator.next();
                necessary = necessary || (predicate.test(value) == false);
                action.accept(new Element<>(necessary, value));
                return true;
            }
            return false;
        }
    }

    private static class TakeWhileSpliterator<T>
            extends AbstractSpliterator<Element<T>> {

        private final Iterator<T> iterator;
        private final Predicate<T> predicate;
        private boolean necessary;

        public TakeWhileSpliterator(Stream<T> stream, Predicate<T> predicate) {
            super(Long.MAX_VALUE, 0);
            this.iterator = Spliterators.iterator(stream.spliterator());
            this.predicate = predicate;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Element<T>> action) {
            if (iterator.hasNext()) {
                T value = iterator.next();
                necessary = necessary || predicate.test(value);
                action.accept(new Element<>(necessary, value));
                return true;
            }
            return false;
        }
    }
}