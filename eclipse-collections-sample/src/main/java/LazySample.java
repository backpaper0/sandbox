import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.impl.factory.Lists;

public class LazySample {

    public static void main(String[] args) {
        accept("list", Lists.immutable.of(1, 2, 3));
        accept("lazy", Lists.immutable.of(1, 2, 3).asLazy());
    }

    static void accept(String title, RichIterable<Integer> ri) {
        System.out.printf("**** %s ****%n", title);
        System.out.printf("injectInto: %s%n", ri.collect(a -> {
            System.out.printf("collect: %s%n", a);
            return a;
        }).select(a -> {
            System.out.printf("select: %s%n", a);
            return true;
        }).tap(a -> System.out.printf("tap: %s%n", a)).injectInto(0, Integer::sum));
    }
}
