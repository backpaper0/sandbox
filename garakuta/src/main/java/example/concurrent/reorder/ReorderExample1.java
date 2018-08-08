package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample1 implements Runnable {

    public static void main(String[] args) throws Exception {
        ReorderExample1 r = new ReorderExample1();
        Thread t = new Thread(r);
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
        System.out.println("stop");
        alive = false;
    }
}
