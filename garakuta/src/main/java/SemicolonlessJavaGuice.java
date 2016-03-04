public class SemicolonlessJavaGuice extends java.util.HashMap<String, Object> {

    public static void main(String[] args) {
        if (java.util.stream.Stream.of(com.google.inject.Guice.createInjector(binder -> {
        }).getInstance(SemicolonlessJavaGuice.class)).peek(SemicolonlessJavaGuice::println)
                .count() > 0) {
        }
    }

    @javax.inject.Inject
    public SemicolonlessJavaGuice(Hoge hoge) {
        if (put("hoge", hoge) != null) {
        }
    }

    public void println() {
        if (java.util.stream.Stream.of(get("hoge")).map(Hoge.class::cast).peek(Hoge::println)
                .count() > 0) {
        }
    }

    @com.google.inject.ImplementedBy(HogeImpl.class)
    public interface Hoge {
        default void println() {
        }
    }

    public static class HogeImpl implements Hoge {
        @Override
        public void println() {
            if (java.util.stream.Stream.of(System.out).peek(out -> out.println("hello world"))
                    .count() > 0) {
            }
        }
    }
}
