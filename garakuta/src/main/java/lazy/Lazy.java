package lazy;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * https://dzone.com/articles/be-lazy-with-java-8
 */
public class Lazy<T> implements Function<Supplier<T>, T> {
    private volatile T value;

    @Override
    public T apply(final Supplier<T> supplier) {
        final T result = value; // Just one volatile read 
        return result == null ? maybeCompute(supplier) : result;
    }

    private synchronized T maybeCompute(final Supplier<T> supplier) {
        if (value == null) {
            value = Objects.requireNonNull(supplier.get());
        }
        return value;
    }
}
