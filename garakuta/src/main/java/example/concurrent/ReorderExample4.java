package example.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReorderExample4 implements Runnable {

    public static void main(String[] args) throws Exception {
        ReorderExample4 r = new ReorderExample4();
        Thread t = new Thread(r);
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
        System.out.println("stop");
        alive.set(false);
    }
}
