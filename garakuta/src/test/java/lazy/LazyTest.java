package lazy;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.Test;

public class LazyTest<T> {

    @Test
    public void testLazy() throws Exception {
        test(Lazy::new);
    }

    @Test
    public void testConcurrentHashMapLazy() throws Exception {
        test(ConcurrentHashMapLazy::new);
    }

    @Test
    public void testFutureTaskLazy() throws Exception {
        test(FutureTaskLazy::new);
    }

    static void test(final Supplier<Function<Supplier<String>, String>> supplier) throws Exception {
        final int size = 100;
        final int unit = 4;
        final CountDownLatch ready = new CountDownLatch(size);
        final CountDownLatch gate = new CountDownLatch(1);

        final List<Function<Supplier<String>, String>> list = IntStream.range(0, unit)
                .mapToObj(i -> supplier.get()).collect(toList());

        final ExecutorService executor = Executors.newFixedThreadPool(size);
        try {

            final List<Future<String>> futures = IntStream.range(0, size)
                    .mapToObj(index -> executor.submit(() -> {
                        ready.countDown();
                        gate.await(10, TimeUnit.SECONDS);
                        return list.get(index % unit).apply(() -> x(() -> {
                            final String s = "#" + index;
                            System.out.println(s);
                            TimeUnit.SECONDS.sleep(1);
                            return s;
                        }));
                    })).collect(toList());

            ready.await(10, TimeUnit.SECONDS);
            gate.countDown();

            System.out.println(futures.stream().map(future -> x(future::get))
                    .collect(groupingBy(identity(), counting())));
        } finally {
            executor.shutdownNow();
        }
    }

    static <T> T x(final Callable<T> c) {
        try {
            return c.call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}