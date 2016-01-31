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

    static void test(Supplier<Function<Supplier<String>, String>> supplier) throws Exception {
        int size = 100;
        int unit = 4;
        CountDownLatch ready = new CountDownLatch(size);
        CountDownLatch gate = new CountDownLatch(1);

        List<Function<Supplier<String>, String>> list = IntStream.range(0, unit)
                .mapToObj(i -> supplier.get()).collect(toList());

        ExecutorService executor = Executors.newFixedThreadPool(size);
        try {

            List<Future<String>> futures = IntStream.range(0, size)
                    .mapToObj(index -> executor.submit(() -> {
                        ready.countDown();
                        gate.await(10, TimeUnit.SECONDS);
                        return list.get(index % unit).apply(() -> x(() -> {
                            String s = "#" + index;
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

    static <T> T x(Callable<T> c) {
        try {
            return c.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}