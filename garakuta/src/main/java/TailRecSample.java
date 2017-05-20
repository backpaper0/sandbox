import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class TailRecSample {

    public static void main(String[] args) {
        System.out.println(sum(1000000));
    }

    static int sum(int n) {
        return sumRec(n).result();
    }

    static TailRec<Integer> sumRec(Integer n) {
        if (n < 1) {
            return TailRec.done(n);
        }
        return TailRec.call(() -> sumRec(n - 1).map(a -> a + n));
    }

    interface TailRec<T> {

        <U> TailRec<U> flatMap(Function<T, TailRec<U>> mapper);
        <R> R accept(Visitor<T, R> visitor);

        default <U> TailRec<U> map(Function<T, U> mapper) {
            return flatMap(t -> TailRec.done(mapper.apply(t)));
        }
        default T result() {
            IsDone<T> isDone = new IsDone<>();
            GetResult<T> getResult = new GetResult<>();
            GetTailRec<T> getTailRec = new GetTailRec<>();
            TailRec<T> tr = this;
            while (true) {
                Boolean done = tr.accept(isDone);
                if (done) {
                    return tr.accept(getResult);
                }
                tr = tr.accept(getTailRec);
            }
        }

        static <T> TailRec<T> call(Supplier<TailRec<T>> supplier) {
            return new Call<>(supplier);
        }
        static <T> TailRec<T> done(T result) {
            return new Done<>(result);
        }
    }

    static class Call<T> implements TailRec<T> {
        private final Supplier<TailRec<T>> supplier;
        public Call(Supplier<TailRec<T>> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }
        @Override
        public <U> TailRec<U> flatMap(Function<T, TailRec<U>> mapper) {
            return new Cont<>(supplier, mapper);
        }
        @Override
        public <R> R accept(Visitor<T, R> visitor) {
            return visitor.visit(this);
        }
    }

    static class Cont<S, T> implements TailRec<T> {
        private final Supplier<TailRec<S>> supplier;
        private final Function<S, TailRec<T>> mapper;
        public Cont(Supplier<TailRec<S>> supplier, Function<S, TailRec<T>> mapper) {
            this.supplier = Objects.requireNonNull(supplier);
            this.mapper = Objects.requireNonNull(mapper);
        }
        @Override
        public <U> TailRec<U> flatMap(Function<T, TailRec<U>> mapper) {
            return TailRec.call(supplier).flatMap(s -> this.mapper.apply(s).flatMap(mapper));
        }
        @Override
        public <R> R accept(Visitor<T, R> visitor) {
            return visitor.visit(this);
        }
    }

    static class Done<T> implements TailRec<T> {
        private final T result;
        public Done(T result) {
            this.result = Objects.requireNonNull(result);
        }
        @Override
        public <U> TailRec<U> flatMap(Function<T, TailRec<U>> mapper) {
            return TailRec.call(() -> mapper.apply(result));
        }
        @Override
        public <R> R accept(Visitor<T, R> visitor) {
            return visitor.visit(this);
        }
    }

    interface Visitor<T, R> {
        R visit(Call<T> call);
        R visit(Done<T> done);
        <S> R visit(Cont<S, T> cont);
    }

    static class IsDone<T> implements Visitor<T, Boolean> {
        @Override
        public Boolean visit(Call<T> call) {
            return false;
        }
        @Override
        public Boolean visit(Done<T> done) {
            return true;
        }
        @Override
        public <S> Boolean visit(Cont<S, T> cont) {
            return false;
        }
    }

    static class GetResult<T> implements Visitor<T, T> {
        @Override
        public T visit(Call<T> call) {
            throw new UnsupportedOperationException();
        }
        @Override
        public T visit(Done<T> done) {
            return done.result;
        }
        @Override
        public <S> T visit(Cont<S, T> cont) {
            throw new UnsupportedOperationException();
        }
    }

    static class GetTailRec<T> implements Visitor<T, TailRec<T>> {
        @Override
        public TailRec<T> visit(Call<T> call) {
            return call.supplier.get();
        }
        @Override
        public TailRec<T> visit(Done<T> done) {
            throw new UnsupportedOperationException();
        }
        @Override
        public <S> TailRec<T> visit(Cont<S, T> cont) {
            return cont.supplier.get().flatMap(cont.mapper);
        }
    }
}
