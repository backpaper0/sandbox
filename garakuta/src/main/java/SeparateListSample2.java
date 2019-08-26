import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeparateListSample2 {

    public static void main(final String[] args) {
        final List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());

        final List<List<Integer>> result = separate(list, 3);

        System.out.println(result);
    }

    static <T> List<List<T>> separate(final List<T> source, final int size) {
        return IntStream.range(0, source.size() / size + Math.min(source.size() % size, 1))
                .map(i -> i * size)
                .mapToObj(i -> source.subList(i, Math.min(i + size, source.size())))
                .collect(Collectors.toList());
    }
}
