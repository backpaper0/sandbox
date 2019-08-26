import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * セミコロンレスJavaで1から100までのIntStreamを作るための試行錯誤
 *
 */
public class IntStreamForSemicolonlessJava {

    public static void main(final String[] args) throws Exception {

        // https://twitter.com/bitter_fox/status/674438084474200066
        final String s1 = Optional.of(SecureRandom.getInstanceStrong())
                .map(r -> IntStream.generate(() -> r.nextInt(101)).distinct()
                        .limit(101).sorted().skip(1))
                .get()
                //
                .mapToObj(String::valueOf).collect(Collectors.joining(" "));
        System.out.println(s1);

        final String s2 = Stream
                .of((ArrayList) Collectors.toList().supplier().get()).peek(
                        list -> list.add(BigInteger.ONE))
                .map(list -> IntStream
                        .generate(
                                () -> Stream.of((BigInteger) list.get(0))
                                        .peek(a -> list.remove(0))
                                        .peek(a -> list
                                                .add(a.add(BigInteger.ONE)))
                                .findFirst().get().intValue()))
                .findFirst().get().limit(100)
                //
                .mapToObj(String::valueOf).collect(Collectors.joining(" "));
        System.out.println(s2);
    }
}
