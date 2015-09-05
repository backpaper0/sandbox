import static java.util.stream.Collectors.joining;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Stream APIでScalaのList.dropのような事をしたいけどそんなメソッドは無い！
 * 
 * <pre>{@code
 * scala> (1 to 10).toList
 * res0: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
 * 
 * scala> res0.drop(5)
 * res1: List[Int] = List(6, 7, 8, 9, 10)
 * 
 * scala>
 * }</pre>
 *
 * なので足掻いてみる。
 * 
 */
public class StreamDropExample {

    public static void main(String[] args) {

        //1,2,3,4,5...みたいな無限ストリームを用意する。
        Stream<Integer> original = Stream.iterate(1, a -> a + 1);

        //先頭から5つ飛ばす
        Stream<Integer> s = drop(original, 5);

        //5つ取り出してカンマ区切りの文字列にする
        String result = s.map(Objects::toString).limit(5)
                .collect(joining(", "));

        System.out.println(result);
    }

    static <T> Stream<T> drop(Stream<T> stream, int n) {
        //インデックス付きのStreamにして
        //要らない分をfilterで取り除いて
        //元の値をmapで取り出す
        IndexedValues<T> sp = new IndexedValues<>(stream);
        return StreamSupport.stream(sp, false)
                .filter(a -> a.isDropped(n) == false).map(IndexedValue::get);
    }

    static class IndexedValue<T> {
        private final int index;
        private final T value;

        public IndexedValue(int index, T value) {
            this.index = index;
            this.value = value;
        }

        public boolean isDropped(int n) {
            return index < n;
        }

        public T get() {
            return value;
        }
    }

    static class IndexedValues<T> extends AbstractSpliterator<IndexedValue<T>> {

        private final Iterator<T> iterator;
        private int count;

        public IndexedValues(Stream<T> stream) {
            super(Long.MAX_VALUE, 0);
            iterator = Spliterators.iterator(stream.spliterator());
        }

        @Override
        public boolean tryAdvance(Consumer<? super IndexedValue<T>> action) {
            if (iterator.hasNext()) {
                action.accept(new IndexedValue<>(count++, iterator.next()));
                return true;
            }
            return false;
        }
    }
}
