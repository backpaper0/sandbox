import java.util.stream.Stream;

public class SemicolonlessJavaEnumMethodSample {

    enum Hoge implements Fuga {
        FOO, BAR, BAZ
    }

    interface Fuga {
        default void println() {
            if (System.out.printf("%s%n", ((Hoge) this).name()) != null) {
            }
        }
    }

    public static void main(final String[] args) {
        if (Stream.of(0).peek(a -> Hoge.FOO.println()).count() > 0) {
        }
        if (Stream.of(0).peek(a -> Hoge.BAR.println()).count() > 0) {
        }
        if (Stream.of(0).peek(a -> Hoge.BAZ.println()).count() > 0) {
        }
    }
}
