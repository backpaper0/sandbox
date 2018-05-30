package example.concurrent;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MapReduceExample {

    public static void main(final String[] args) {

        final PrintWriter out = new PrintWriter(System.out);

        final List<String> list = Arrays.asList("foo", "bar", "baz", "hoge", "foobar");

        final Function<String, Integer> mapper = s -> {
            out.printf("mapper: %s%n", Thread.currentThread().getName());
            return s.length();
        };

        final BinaryOperator<Integer> reducer = (a, b) -> {
            out.printf("reducer: %s%n", Thread.currentThread().getName());
            return a + b;
        };

        final Integer result = MapReducer.mapReduce(list, mapper, reducer);
        out.printf("result: %s%n", result);
        out.flush();
    }

    private static class MapReducer<T, U> extends CountedCompleter<U> {

        private final List<T> list;

        private final int low;

        private final int high;

        private final Function<T, U> mapper;

        private final BinaryOperator<U> reducer;

        private MapReducer<T, U> sibling;

        private U result;

        private MapReducer(final MapReducer<T, U> parent, final List<T> list, final int low,
                final int high, final Function<T, U> mapper, final BinaryOperator<U> reducer) {
            super(parent);
            this.list = list;
            this.low = low;
            this.high = high;
            this.mapper = mapper;
            this.reducer = reducer;
        }

        @Override
        public void compute() {
            if (high - low > 1) {
                final int mid = (low + high) / 2;

                final MapReducer<T, U> left = new MapReducer<>(this, list, low, mid,
                        mapper, reducer);
                final MapReducer<T, U> right = new MapReducer<>(this, list, mid, high,
                        mapper, reducer);

                left.sibling = right;
                right.sibling = left;

                //保留アクションの数をセットする
                setPendingCount(2);

                left.fork();
                right.fork();

            } else if (low < high) {
                final T element = list.get(low);
                result = mapper.apply(element);
            }

            //保留アクション数が1以上なら1減らす
            //0ならonCompletionを実行する
            tryComplete();
        }

        @Override
        public void onCompletion(final CountedCompleter<?> caller) {
            if (this != caller) {
                final MapReducer<T, U> child = (MapReducer<T, U>) caller;
                final MapReducer<T, U> sibling = child.sibling;
                if (sibling != null && sibling.result != null) {
                    result = reducer.apply(child.result, sibling.result);
                } else {
                    result = child.result;
                }
            }
        }

        @Override
        public U getRawResult() {
            return result;
        }

        public static <T, U> U mapReduce(final List<T> list, final Function<T, U> mapper,
                final BinaryOperator<U> reducer) {
            return new MapReducer<>(null, list, 0, list.size(), mapper, reducer).invoke();
        }
    }
}