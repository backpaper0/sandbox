package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReorderExample4 implements Runnable {

    public static void main(final String[] args) throws Exception {
        final ReorderExample4 r = new ReorderExample4();
        final Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(1);
        r.stop();
    }

    //AtomicBooleanを使ってもメモリの同期はなされる
    private final AtomicBoolean alive = new AtomicBoolean(true);

    @Override
    public void run() {
        long counter = 0L;
        while (isAlive()) {
            counter++;
        }
        System.out.printf("%,d", counter);
    }

    public boolean isAlive() {
        return alive.get();
    }

    public void stop() {
        alive.set(false);
        System.out.println("stop");
    }
}
