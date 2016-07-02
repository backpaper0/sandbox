package util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Functions {

    private Functions() {
    }

    public static <T, U> Predicate<T> by(Function<? super T, ? extends U> mapper,
            Predicate<? super U> predicate) {
        return t -> predicate.test(mapper.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> filterMap(Predicate<? super T> predicate,
            Function<? super T, ? extends R> mapper) {
        return t -> predicate.test(t) ? Stream.of(mapper.apply(t)) : Stream.empty();
    }

    public static <T, U, R> Function<U, R> applyPartial(
            BiFunction<? super T, ? super U, ? extends R> f, T t) {
        return u -> f.apply(t, u);
    }
}
