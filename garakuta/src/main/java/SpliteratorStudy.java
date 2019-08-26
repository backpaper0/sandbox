import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SpliteratorStudy {

    public static void main(final String[] args) {
        final Stream<Integer> stream = StreamSupport.stream(new CountSpliterator(),
                true);
        final String s = stream.parallel().limit(100).map(Object::toString)
                .collect(Collectors.joining(","));
        System.out.println(s);
    }

    static class CountSpliterator extends AbstractSpliterator<Integer> {

        private final AtomicInteger counter;
        private boolean splitted;

        public CountSpliterator() {
            this(new AtomicInteger(0), false);
        }

        public CountSpliterator(final AtomicInteger counter, final boolean splitted) {
            super(Long.MAX_VALUE, Spliterator.CONCURRENT | Spliterator.DISTINCT
                    | Spliterator.NONNULL);
            this.counter = counter;
            this.splitted = splitted;
        }

        @Override
        public boolean tryAdvance(final Consumer<? super Integer> action) {
            action.accept(counter.getAndIncrement());
            return true;
        }

        @Override
        public Spliterator<Integer> trySplit() {
            if (splitted) {
                return null;
            }
            splitted = true;
            return new CountSpliterator(counter, splitted);
        }
    }
}
