import java.util.Objects;

public class Java10 {

    public static void main(final String[] args) throws Exception {
        final var bar = new Bar<>("Hello, Java 10 !!!", "close") {
            @Override
            public void println() {
                println(getMessage());
            }
        };
        try (bar) {
            bar.println();
        }
    }

    static AutoCloseable newAutoCloseableInstance() {
        return () -> System.out.println("close");
    }

    interface Foo {

        private void printlnInternal(final String message) {
            System.out.println(message);
        }

        default void println(final String message) {
            printlnInternal(message);
        }
    }

    static abstract class Bar<T> implements AutoCloseable, Foo {

        private final String message;
        private final T closeMessage;

        public Bar(final String message, final T closeMessage) {
            this.message = Objects.requireNonNull(message);
            this.closeMessage = Objects.requireNonNull(closeMessage);
        }

        public abstract void println();

        protected String getMessage() {
            return message;
        }

        @Override
        public void close() {
            System.out.println(closeMessage);
        }
    }
}
