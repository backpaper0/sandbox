package util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Functions {

    private Functions() {
    }

    /**
     * mapしてtestするためのユーティリティです。
     * 
     * <pre><code>
     * stream.filter(by(Foo::toBar, Bar::isBaz));
     * </code></pre>
     * 
     * @param mapper
     * @param predicate
     * @return
     * @see Stream#filter(Predicate)
     */
    public static <T, U> Predicate<T> by(final Function<? super T, ? extends U> mapper,
            final Predicate<? super U> predicate) {
        return t -> predicate.test(mapper.apply(t));
    }

    /**
     * testしてmapするためのユーティリティです。
     * 
     * <pre><code>
     * stream.flatMap(filterMap(Foo::isBar, Foo::toBaz));
     * </code></pre>
     * 
     * @param predicate
     * @param mapper
     * @return
     * @see Stream#flatMap(Function)
     */
    //TODO もっと良い名前つけたい
    public static <T, R> Function<T, Stream<R>> filterMap(final Predicate<? super T> predicate,
            final Function<? super T, ? extends R> mapper) {
        return t -> predicate.test(t) ? Stream.of(mapper.apply(t)) : Stream.empty();
    }

    /**
     * 2引数の関数の第1引数だけ適用した状態の関数を返すユーティリティです。
     * 
     * <pre><code>
     * IntStream.of(1, 2, 3).map(apply(Integer::sum, 4));
     * //5, 6, 7
     * </code></pre>
     * 
     * @param mapper
     * @param t
     * @return
     * @see Stream#map(Function)
     */
    //TODO もっと良い名前つけたい
    public static <T, U, R> Function<U, R> applyPartial(
            final BiFunction<? super T, ? super U, ? extends R> mapper, final T t) {
        return u -> mapper.apply(t, u);
    }
}
