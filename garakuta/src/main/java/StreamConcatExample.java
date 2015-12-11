import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * シーケンシャルストリームとパラレルストリームをStream.concatしたらどうなるのか確認してみた
 *
 */
public class StreamConcatExample {

    public static void main(String[] args) {

        Stream<int[]> a = stream(0);
        Stream<int[]> b = stream(1).parallel();
        Stream<int[]> c = stream(2);
        Stream<int[]> d = stream(3).parallel();

        Stream<int[]> e = Stream.concat(a, b);
        Stream<int[]> f = Stream.concat(c, d);

        Stream<int[]> g = Stream.concat(e, f);

        g.parallel().forEach(x -> {
            System.out.printf("%s%s:%s ( %s )%n",
                    IntStream.range(0, x[0]).mapToObj(unused -> "    ")
                            .collect(Collectors.joining()),
                    x[0], x[1], Thread.currentThread().getName());
        });
    }

    static Stream<int[]> stream(int index) {
        return IntStream.range(0, 10).mapToObj(x -> new int[] { index, x })
                .peek(x -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(
                                ThreadLocalRandom.current().nextInt(1000));
                    } catch (Exception e) {
                    }
                });
    }
}
