import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class BasicSample {

    public static void main(String[] args) {

        ImmutableList<Integer> list = Lists.immutable.of(1, 2, 3, 2, 3, 3);

        // filter
        System.out.println(list.select(i -> i % 2 != 0));

        // map
        System.out.println(list.collect(Integer::toBinaryString));

        // flatMap
        System.out.println(list.flatCollect(i -> Lists.immutable.of(i, i)));

        // distinct
        System.out.println(list.distinct());

        // limit
        System.out.println(list.take(4));

        // skip
        System.out.println(list.drop(2));

        // reduce
        System.out.println(list.injectInto(0, Integer::sum));

        // min (ただし戻り値は Optional<T> ではなく T )
        System.out.println(list.min());

        // max (ただし戻り値は Optional<T> ではなく T )
        System.out.println(list.max());

        // count
        System.out.println(list.count(unused -> true));

        // findFirst (ただし戻り値は Optional<T> ではなく T )
        System.out.println(list.getFirst());

        // allMatch
        System.out.println(list.allSatisfy(i -> i < 4));

        // anyMatch
        System.out.println(list.anySatisfy(i -> i < 3));
    }
}
