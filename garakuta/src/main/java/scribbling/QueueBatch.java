package scribbling;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueBatch {

    public static void main(final String[] args) throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            queue.add(new Queueing(queue));
            for (int i = 0; i < 30; i++) {
                final Runnable take = queue.take();
                executor.execute(take);
            }

        } finally {
            executor.shutdown();
        }
    }

    static class Queueing implements Runnable {

        private final BlockingQueue<Runnable> queue;

        public Queueing(final BlockingQueue<Runnable> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.printf("%s(%s)%n", getClass().getSimpleName(), hashCode());
            for (int i = 0; i < 3; i++) {
                sleep();
                queue.add(new Task(i));
            }
            sleep();
            queue.add(this);
        }
    }

    static class Task implements Runnable {

        private final int value;

        public Task(final int value) {
            this.value = value;
        }

        @Override
        public void run() {
            System.out.printf("%s(%s: %s)%n", getClass().getSimpleName(), value, hashCode());
            sleep();
        }
    }

    static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (final InterruptedException e) {
        }
    }
}
