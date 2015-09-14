import java.util.Objects;
import java.util.function.Function;

public class MonaMona {

    public static void main(String[] args) {
        System.out.println(Maybe.just(100).fmap(a -> a * 2));
        System.out.println(Maybe.<Integer> nothing().fmap(a -> a * 2));

        System.out.println(List.of(1, 2, 3));
        System.out.println(List.of(1, 2, 3).fmap(a -> a * 2));
    }

    interface List<A> extends Functor<A, List<?>> {
        @Override
        <B> List<B> fmap(Function<A, B> f);

        Maybe<A> head();

        List<A> tail();

        static <A> List<A> of(A... values) {
            return of((List<A>) Nil.INSTANCE, values.length - 1, values);
        }

        static <A> List<A> of(List<A> as, int index, A... values) {
            return index < 0 ? as
                    : of(new Cons<>(values[index], as), index - 1, values);
        }
    }

    static class Cons<A> implements List<A> {
        private final A head;
        private final List<A> tail;

        public Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public <B> List<B> fmap(Function<A, B> f) {
            return new Cons<>(f.apply(head), tail.fmap(f));
        }

        @Override
        public Maybe<A> head() {
            return Maybe.just(head);
        }

        @Override
        public List<A> tail() {
            return tail;
        }

        @Override
        public String toString() {
            return String.format("%s :: %s", head, tail);
        }
    }

    enum Nil implements List<Object> {
        INSTANCE;
        @Override
        public <B> List<B> fmap(Function<Object, B> f) {
            return (List<B>) this;
        }

        @Override
        public Maybe<Object> head() {
            return Maybe.nothing();
        }

        @Override
        public List<Object> tail() {
            return this;
        }

        @Override
        public String toString() {
            return "Nil";
        }
    }

    interface Maybe<A> extends Functor<A, Maybe<?>> {
        static <A> Maybe<A> just(A value) {
            return new Just<>(value);
        }

        static <A> Maybe<A> nothing() {
            return (Maybe<A>) Nothing.INSTANCE;
        }
    }

    static class Just<A> implements Maybe<A> {

        private final A value;

        public Just(A value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public <B> Maybe<B> fmap(Function<A, B> f) {
            return new Just<>(f.apply(value));
        }

        @Override
        public String toString() {
            return String.format("Just(%s)", value);
        }
    }

    enum Nothing implements Maybe<Object> {
        INSTANCE;
        @Override
        public <B> Maybe<B> fmap(Function<Object, B> f) {
            return (Maybe<B>) this;
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    interface Functor<A, F extends Functor<?, F>> {
        <B> F fmap(Function<A, B> f);
    }
}
