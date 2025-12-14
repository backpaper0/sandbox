import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class App2 {
    public static void main(String[] args) throws Exception {

        CountDownLatch gate = new CountDownLatch(1);

        Thread t1 = Thread.ofVirtual().unstarted(() -> {
            synchronized (gate) {
                System.out.println("a-1");

                gate.countDown();

                try {
                    var url = URI.create("http://localhost:8080/delay/1").toURL();
                    var conn = url.openConnection();
                    try (var in = conn.getInputStream()) {
                        in.readAllBytes();
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }

                System.out.println("a-2");
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
