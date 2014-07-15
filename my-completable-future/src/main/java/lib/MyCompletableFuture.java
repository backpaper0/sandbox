package lib;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class MyCompletableFuture<T> {

    private volatile T result;

    private Runnable action;

    private boolean prepared;

    private boolean completed;

    private final ReentrantLock lock = new ReentrantLock();

    private MyCompletableFuture() {
    }

    private void complete(T result) {
        lock.lock();
        try {
            if (completed) {
                throw new IllegalStateException();
            }
            this.result = result;
            if (prepared) {
                action.run();
            }
            completed = true;
        } finally {
            lock.unlock();
        }
    }

    private void prepare(Runnable action) {
        lock.lock();
        try {
            if (prepared) {
                throw new IllegalStateException();
            }
            this.action = action;
            if (completed) {
                action.run();
            }
            prepared = true;
        } finally {
            lock.unlock();
        }
    }

    public static <U> MyCompletableFuture<U> completedFuture(U value) {
        MyCompletableFuture<U> future = new MyCompletableFuture<>();
        future.complete(value);
        return future;
    }

    public static <U> MyCompletableFuture<U> supplyAsync(
            final MySupplier<U> supplier, final Executor executor) {
        final MyCompletableFuture<U> future = new MyCompletableFuture<>();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                future.complete(supplier.get());
            }
        });
        return future;
    }

    public <U> MyCompletableFuture<U> thenApplyAsync(MyFunction<T, U> fn,
            Executor executor) {
        MyCompletableFuture<U> next = new MyCompletableFuture<>();
        ApplyAsync.prepare(this, next, fn, executor);
        return next;
    }

    public <U> MyCompletableFuture<U> applyToEitherAsync(
            MyCompletableFuture<T> other, MyFunction<T, U> fn, Executor executor) {
        AtomicBoolean run = new AtomicBoolean(false);
        MyCompletableFuture<U> next = new MyCompletableFuture<>();
        EitherAsync.prepare(this, next, fn, run, executor);
        EitherAsync.prepare(other, next, fn, run, executor);
        return next;
    }

    public <U, V> MyCompletableFuture<V> thenCombineAsync(
            final MyCompletableFuture<U> other, MyBiFunction<T, U, V> fn,
            Executor executor) {
        MyCompletableFuture<V> next = new MyCompletableFuture<>();
        final AtomicReference<T> t = new AtomicReference<>();
        final AtomicReference<U> u = new AtomicReference<>();
        AtomicInteger count = new AtomicInteger(0);
        CombineAsync.prepare(this, new Runnable() {

            @Override
            public void run() {
                t.set(result);
            }
        }, next, fn, t, u, count, executor);
        CombineAsync.prepare(other, new Runnable() {

            @Override
            public void run() {
                u.set(other.result);
            }
        }, next, fn, t, u, count, executor);
        return next;
    }

    public interface MySupplier<R> {

        R get();
    }

    public interface MyFunction<A, R> {

        R apply(A a);
    }

    public interface MyBiFunction<T, U, R> {

        R apply(T t, U u);
    }

    private static class ApplyAsync<T, U> implements Runnable {

        private final MyCompletableFuture<T> current;

        private final MyCompletableFuture<U> next;

        private final MyFunction<T, U> fn;

        private final Executor executor;

        private ApplyAsync(MyCompletableFuture<T> current,
                MyCompletableFuture<U> next, MyFunction<T, U> fn,
                Executor executor) {
            this.current = current;
            this.next = next;
            this.fn = fn;
            this.executor = executor;
        }

        @Override
        public void run() {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    next.complete(fn.apply(current.result));
                }
            });
        }

        public static <T, U> void prepare(MyCompletableFuture<T> current,
                MyCompletableFuture<U> next, MyFunction<T, U> fn,
                Executor executor) {
            current.prepare(new ApplyAsync<>(current, next, fn, executor));
        }
    }

    private static class EitherAsync<T, U> implements Runnable {

        private final MyCompletableFuture<T> current;

        private final MyCompletableFuture<U> next;

        private final MyFunction<T, U> fn;

        private final AtomicBoolean run;

        private final Executor executor;

        private EitherAsync(MyCompletableFuture<T> current,
                MyCompletableFuture<U> next, MyFunction<T, U> fn,
                AtomicBoolean run, Executor executor) {
            this.current = current;
            this.next = next;
            this.fn = fn;
            this.run = run;
            this.executor = executor;
        }

        @Override
        public void run() {
            if (run.compareAndSet(false, true)) {
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        next.complete(fn.apply(current.result));
                    }
                });
            }
        }

        public static <T, U> void prepare(MyCompletableFuture<T> current,
                MyCompletableFuture<U> next, MyFunction<T, U> fn,
                AtomicBoolean run, Executor executor) {
            current.prepare(new EitherAsync<>(current, next, fn, run, executor));
        }
    }

    private static class CombineAsync<T, U, V> implements Runnable {

        private final Runnable resultAction;

        private final MyCompletableFuture<V> next;

        private final MyBiFunction<T, U, V> fn;

        private final AtomicReference<T> t;

        private final AtomicReference<U> u;

        private final AtomicInteger count;

        private final Executor executor;

        private CombineAsync(Runnable resultAction,
                MyCompletableFuture<V> next, MyBiFunction<T, U, V> fn,
                AtomicReference<T> t, AtomicReference<U> u,
                AtomicInteger count, Executor executor) {
            this.resultAction = resultAction;
            this.next = next;
            this.fn = fn;
            this.t = t;
            this.u = u;
            this.count = count;
            this.executor = executor;
        }

        @Override
        public void run() {
            resultAction.run();
            if (count.incrementAndGet() == 2) {
                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        next.complete(fn.apply(t.get(), u.get()));
                    }
                });
            }
        }

        public static <T, U, V> void prepare(MyCompletableFuture<?> current,
                Runnable resultAction, MyCompletableFuture<V> next,
                MyBiFunction<T, U, V> fn, AtomicReference<T> t,
                AtomicReference<U> u, AtomicInteger count, Executor executor) {
            current.prepare(new CombineAsync<>(resultAction, next, fn, t, u,
                    count, executor));
        }
    }
}
