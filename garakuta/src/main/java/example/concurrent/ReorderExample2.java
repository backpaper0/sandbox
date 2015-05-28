package example.concurrent;

import java.util.concurrent.TimeUnit;

public class ReorderExample2 implements Runnable {

    public static void main(String[] args) throws Exception {
        ReorderExample2 r = new ReorderExample2();
        Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(1);
        r.stop();
    }

    //volatileを付けてaliveを使用するときは必ずメモリを同期する
    private volatile boolean alive = true;

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
