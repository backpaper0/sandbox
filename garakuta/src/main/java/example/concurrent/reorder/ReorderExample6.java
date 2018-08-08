package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class ReorderExample6 implements Runnable {

    public static void main(final String[] args) throws Exception {
        final ReorderExample6 r = new ReorderExample6();
        final Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(1);
        r.stop();
    }

    private boolean alive = true;
    private final StampedLock lock = new StampedLock();

    @Override
    public void run() {
        long counter = 0L;
        while (isAlive()) {
            counter++;
        }
        System.out.printf("%,d", counter);
    }

    private boolean isAlive() {
        //スタンプを取得して処理を行う
        long stamp = lock.tryOptimisticRead();
        boolean b = alive;
        //スタンプの取得からここまでで書き込みロックが取得されていないか検証する
        if (lock.validate(stamp) == false) {
            //書き込みロックが取得されていた場合は読み取りロックを取得して再度処理を行う
            stamp = lock.readLock();
            try {
                b = alive;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return b;
    }

    public void stop() {
        //書き込みロックを取得して処理を行う
        final long stamp = lock.writeLock();
        try {
            alive = false;
        } finally {
            lock.unlockWrite(stamp);
        }
        System.out.println("stop");
    }
}
