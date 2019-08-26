import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MethodExample implements IMethodExample {

    public static void main(final String[] args) {
        Stream.of(MethodExample.class, Inner.class)
                .flatMap(a -> Arrays.stream(a.getDeclaredMethods()))
                .forEach(m -> {
                    System.out.println("----------------");
                    System.out.println(m.toGenericString());
                    System.out.printf("isBridge   : %s%n", m.isBridge());
                    System.out.printf("isSynthetic: %s%n", m.isSynthetic());
                });

        new MethodExample().new Inner().privateMethod();
    }

    UnaryOperator<Integer> f = i -> i;

    @Override
    public Integer get() {
        return 0;
    }

    class Inner {
        private void privateMethod() {
        }
    }
}

interface IMethodExample {
    Number get();
}
