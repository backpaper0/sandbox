package lazy;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrentHashMapLazy<T> implements Function<Supplier<T>, T> {

    private final ConcurrentHashMap<Integer, T> cache = new ConcurrentHashMap<>(1);

    @Override
    public T apply(Supplier<T> t) {
        return cache.computeIfAbsent(0, a -> Objects.requireNonNull(t.get()));
    }
}