import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeparateListSample {

    public static void main(final String[] args) {
        final List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());

        final List<List<Integer>> result = list.stream().collect(separate(3));

        System.out.println(result);
    }

    static <T> Collector<T, ?, List<List<T>>> separate(final int size) {
        return Collector.of(() -> {
            final List<List<T>> acc = new ArrayList<>();
            acc.add(new ArrayList<>());
            return acc;
        }, (acc, x) -> {
            List<T> l = acc.get(acc.size() - 1);
            if (l.size() >= size) {
                acc.add(l = new ArrayList<>());
            }
            l.add(x);
        }, (l, r) -> {
            l.addAll(r);
            return l;
        });
    }
}
