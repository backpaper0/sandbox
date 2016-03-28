import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComparatorStudy {

    public static void main(String[] args) {
        List<Hoge> list = Stream.of(new Hoge(2), new Hoge(3), new Hoge(null), new Hoge(1))
                .sorted(Comparator.comparing((Hoge x) -> x.value != null)
                        .thenComparing(Comparator.comparing(x -> x.value)).reversed())
                .collect(Collectors.toList());
        System.out.println(list);
    }

    static class Hoge {

        Integer value;

        public Hoge(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Hoge(" + value + ")";
        }
    }
}
