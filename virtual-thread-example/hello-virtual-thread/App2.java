import java.util.concurrent.CountDownLatch;

public class App2 {
    public static void main(String[] args) throws Exception {

        CountDownLatch gate = new CountDownLatch(1);

        Thread t1 = Thread.ofVirtual().unstarted(() -> {
            synchronized (gate) {
                System.out.println("a-1");

                gate.countDown();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("a-2");
            }
        });

        Thread t2 = Thread.ofVirtual().unstarted(() -> {
            System.out.println("b-1");

            try {
                gate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("b-2");
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
