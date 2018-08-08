package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample1 implements Runnable {

    public static void main(final String[] args) throws Exception {
        final ReorderExample1 r = new ReorderExample1();
        final Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(1);
        r.stop();
    }

    private boolean alive = true;

    @Override
    public void run() {
        long counter = 0L;
        while (isAlive()) {
            counter++;
        }
        System.out.printf("%,d", counter);
    }

    private boolean isAlive() {
        return alive;
    }

    public void stop() {
        alive = false;
        System.out.println("stop");
    }
}
