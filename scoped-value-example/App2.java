import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.StructuredTaskScope.Subtask;

public class App2 {
    public static void main(String[] args) {
        var sv1 = ScopedValue.newInstance();
        ScopedValue.where(sv1, "a").run(() -> {
            try (var scope = StructuredTaskScope.open()) {
                var cb = new CyclicBarrier(3);
                Callable<? extends Long> task = () -> {
                    try {
                        cb.await(1, TimeUnit.SECONDS);
                    } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                    // 値が別スレッドへ継承されていることを確認。
                    assert Objects.equals(sv1.get(), "a");
                    assert Thread.currentThread().isVirtual();
                    return Thread.currentThread().threadId();
                };
                var st1 = scope.fork(task);
                var st2 = scope.fork(task);
                var st3 = scope.fork(task);
                scope.join();

                // 異なるスレッドであることを確認。
                assert Objects.equals(st1.get(), st2.get()) == false;
                assert Objects.equals(st1.get(), st3.get()) == false;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
