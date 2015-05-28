package example.concurrent;

import java.util.concurrent.TimeUnit;

public class ReorderExample3 implements Runnable {

    public static void main(String[] args) throws Exception {
        ReorderExample3 r = new ReorderExample3();
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

    //aliveを使用している箇所をsynchronizedメソッド/ブロックにしてメモリを同期する

    private synchronized boolean isAlive() {
        return alive;
    }

    public void stop() {
        System.out.println("stop");
        synchronized (this) {
            alive = false;
        }
    }
}
