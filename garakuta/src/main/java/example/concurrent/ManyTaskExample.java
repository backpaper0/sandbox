package example.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ManyTaskExample {

    static Random r = new Random();

    public static void main(String[] args) throws Exception {

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CompletableFuture<Void> future = CompletableFuture
                    .runAsync(message(i));
            futures.add(future);
        }

        CompletableFuture<Void> future = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture<?>[futures.size()]));

        future.thenRunAsync(message("finish")).get();
    }

    static Runnable message(Object text) {
        return () -> {
            try {
                Thread.sleep(r.nextInt(100));
            } catch (InterruptedException e) {
            }
            System.out.println(text);
        };
    }
}
