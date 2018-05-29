package example;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ReadWriteLockExample {

    public static void main(final String[] args) throws Exception {
        final ReadWriteLock rwl = new ReentrantReadWriteLock();
        final int size = 3;
        final ExecutorService executor = Executors.newFixedThreadPool(size);
        try {
            final ReadWriteLockExample obj = new ReadWriteLockExample(rwl, size, executor);
            obj.example1();
            obj.example2();
        } finally {
            executor.shutdown();
        }
    }

    private final ReadWriteLock rwl;
    private final int size;
    private final ExecutorService executor;

    private ReadWriteLockExample(final ReadWriteLock rwl, final int size,
            final ExecutorService executor) {
        this.rwl = rwl;
        this.size = size;
        this.executor = executor;
    }

    private void example1() throws Exception {
        System.out.println("**** example 1 ****");
        final CountDownLatch ready = new CountDownLatch(size);
        final CountDownLatch go = new CountDownLatch(1);
        final Callable<Void> task = () -> {
            ready.countDown();
            go.await();
            final Lock lock = rwl.readLock();
            lock.lock();
            try {
                System.out.printf("begin: %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.printf("end: %s%n", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
            return null;
        };
        final List<Future<Void>> fetures = IntStream.range(0, size)
                .mapToObj(i -> executor.submit(task))
                .collect(Collectors.toList());
        ready.await();
        go.countDown();
        for (final Future<Void> future : fetures) {
            future.get();
        }
    }

    private void example2() throws Exception {
        System.out.println("**** example 2 ****");
        final CountDownLatch ready = new CountDownLatch(size);
        final CountDownLatch go = new CountDownLatch(1);

        final Callable<Void> readTask = () -> {
            ready.countDown();
            go.await();
            TimeUnit.SECONDS.sleep(1);
            final Lock lock = rwl.readLock();
            lock.lock();
            try {
                System.out.printf("[r]begin: %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.printf("[r]end: %s%n", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
            return null;
        };

        final Callable<Void> writeTask = () -> {
            ready.countDown();
            go.await();
            final Lock lock = rwl.writeLock();
            lock.lock();
            try {
                System.out.printf("[w]begin: %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
                System.out.printf("[w]end: %s%n", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
            return null;
        };

        final List<Future<Void>> fetures = Stream.concat(
                IntStream.range(0, size - 1).mapToObj(i -> executor.submit(readTask)),
                Stream.of(executor.submit(writeTask)))
                .collect(Collectors.toList());
        ready.await();
        go.countDown();
        for (final Future<Void> future : fetures) {
            future.get();
        }
    }
}
