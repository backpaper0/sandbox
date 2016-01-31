package lazy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class FutureTaskLazy<T> implements Function<Supplier<T>, T> {

    private final AtomicReference<FutureTask<T>> ref = new AtomicReference<>();

    @Override
    public T apply(Supplier<T> t) {
        FutureTask<T> future = new FutureTask<>(t::get);
        if (ref.compareAndSet(null, future)) {
            future.run();
        }
        try {
            return ref.get().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}