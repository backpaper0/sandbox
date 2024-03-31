import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CountDownLatch;

public class App3 {
    public static void main(String[] args) throws Exception {

        CountDownLatch gate = new CountDownLatch(1);

        Thread t1 = Thread.ofVirtual().unstarted(() -> {
            System.out.println("a-1");

            gate.countDown();

            try (var in = new FileInputStream("App3.java")) {
                in.readAllBytes();
                System.out.println("a-2");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        Thread t2 = Thread.ofVirtual().unstarted(() -> {
            System.out.println("b-1");
        });

        t1.start();
        gate.await();
        t2.start();

        t1.join();
        t2.join();
    }
}
