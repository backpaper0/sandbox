import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamGroupingBySample {

    public static void main(String[] args) {

        Stream<Hoge> stream = Stream.of(new Hoge("x", "a"), new Hoge("x", "b"), new Hoge("x", "c"),
                new Hoge("y", "a"), new Hoge("y", "d"));

        Map<String, String> map = stream.collect(Collectors.groupingBy(x -> x.foo,
                Collectors.reducing("", x -> x.bar, String::concat)));

        System.out.println(map);
    }

    static class Hoge {
        String foo;
        String bar;

        public Hoge(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }
}
