import java.util.function.Function;

/**
 * 関数インターフェースでペアを表現する。
 *
 */
public class FunctionalPair {

    public static void main(final String[] args) {

        final Pair<Integer, String> status = Pair.of(200, "OK");

        System.out.println(status.first()); //200
        System.out.println(status.second()); //"OK"
    }

    //ジェネリクスで A or B の表現ができないので Object をバインドしている
    @FunctionalInterface
    interface Pair<A, B> extends
            Function<Function<A, Function<B, Object>>, Object> {

        static <A, B> Pair<A, B> of(final A a, final B b) {
            return f -> f.apply(a).apply(b);
        }

        default A first() {
            return (A) apply(a -> b -> a);
        }

        default B second() {
            return (B) apply(a -> b -> b);
        }
    }
}