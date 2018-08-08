package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReorderExample5 implements Runnable {

    public static void main(final String[] args) throws Exception {
        final ReorderExample5 r = new ReorderExample5();
        final Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(1);
        r.stop();
    }

    private boolean alive = true;
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    @Override
    public void run() {
        long counter = 0L;
        while (isAlive()) {
            counter++;
        }
        System.out.printf("%,d", counter);
    }

    private boolean isAlive() {
        final Lock lock = rwl.readLock();
        lock.lock();
        try {
            return alive;
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        final Lock lock = rwl.writeLock();
        lock.lock();
        try {
            alive = false;
        } finally {
            lock.unlock();
        }
        System.out.println("stop");
    }
}
