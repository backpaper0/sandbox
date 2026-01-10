import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class App1 {
    public static void main(String[] args) throws InterruptedException {
        var sv1 = ScopedValue.newInstance();

        // ごく普通の使い方。
        ScopedValue.where(sv1, "a").run(() -> {
            assert Objects.equals(sv1.get(), "a");
        });

        // call()で値を返すことも可能。
        var rv1 = ScopedValue.where(sv1, "b").call(() -> {
            return sv1.get();
        });
        assert Objects.equals(rv1, "b");

        // whereで値を設定していないとNoSuchElementExceptionがスローされる。
        try {
            sv1.get();
            assert false;
        } catch (NoSuchElementException e) {
        }

        // orElseもある。
        assert Objects.equals(sv1.orElse("c"), "c");

        // orElseThrowもある。
        try {
            sv1.orElseThrow(IOException::new);
            assert false;
        } catch (IOException e) {
        }

        // 値が設定されているかどうかを確認できる
        ScopedValue.where(sv1, "d").run(() -> {
            assert sv1.isBound();
        });
        assert sv1.isBound() == false;

        // スレッド間で値を継承しない。
        ScopedValue.where(sv1, "e").run(() -> {
            try {
                Thread.startVirtualThread(() -> {
                    try {
                        sv1.get();
                        assert false;
                    } catch (NoSuchElementException e) {
                    }
                }).join();
            } catch (InterruptedException e) {
            }
        });

        // 同じScopedValueでも異なるスレッドだと異なる値を設定する。
        var cb1 = new CyclicBarrier(2);
        var cb2 = new CyclicBarrier(2);
        var t1 = Thread.startVirtualThread(() -> {
            ScopedValue.where(sv1, "f").run(() -> {
                try {
                    cb1.await(1, TimeUnit.SECONDS);
                    assert Objects.equals(sv1.get(), "f");
                    cb2.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        var t2 = Thread.startVirtualThread(() -> {
            ScopedValue.where(sv1, "g").run(() -> {
                try {
                    cb1.await(1, TimeUnit.SECONDS);
                    assert Objects.equals(sv1.get(), "g");
                    cb2.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        t1.join();
        t2.join();

        // 設定していないScopedValueからは値が取れない。
        var sv2 = ScopedValue.newInstance();
        ScopedValue.where(sv1, "h").run(() -> {
            try {
                sv2.get();
                assert false;
            } catch (NoSuchElementException e) {
            }
        });

        // 複数のScopedValueへ値を設定する。
        ScopedValue.where(sv1, "i").where(sv2, "j").run(() -> {
            assert Objects.equals(sv1.get(), "i");
            assert Objects.equals(sv2.get(), "j");
        });

        // ネストできる。
        ScopedValue.where(sv1, "k").run(() -> {
            assert Objects.equals(sv1.get(), "k");
            ScopedValue.where(sv1, "l").run(() -> {
                assert Objects.equals(sv1.get(), "l");
            });
            assert Objects.equals(sv1.get(), "k");
        });
    }
}
