
public interface SemicolonlessKiyoshi {

    static void main(final String[] args) {
        if (java.util.stream.Stream.of(new java.util.Random())
                .map(r -> (java.util.function.Supplier<String>) () -> r.nextBoolean() ? "ズン" : "ドコ")
                .flatMap(r -> java.util.stream.Stream.iterate(r.get(), s -> s + r.get())
                        .filter(s -> s.endsWith("ズンズンズンズンドコ")).map(s -> s + "キ・ヨ・シ！").limit(1))
                .peek(System.out::println).count() > 0) {
        }
    }
}
