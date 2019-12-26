
import static org.junit.jupiter.api.Assertions.*;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

import org.junit.jupiter.api.Test;

class ReferenceTest {

    @Test
    void test_WeakReference() throws Exception {

        //finalizeが呼ばれることを確認するために使用するラッチ
        final CountDownLatch finalized = new CountDownLatch(1);

        final ReferenceQueue<Object> q = new ReferenceQueue<>();

        /*
         * 弱い参照
         */
        final Reference<Object> ref = new WeakReference<>(new Object() {

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                finalized.countDown();
            }
        }, q);

        assertFalse(ref.isEnqueued());

        //空きメモリが増えなくなるなるまでGCしまくる
        final LongSupplier f = () -> Runtime.getRuntime().freeMemory();
        long freeMemory = f.getAsLong();
        do {
            System.gc();
            System.out.println("GCしたよ");
        } while (freeMemory < (freeMemory = f.getAsLong()));

        assertTrue(ref.isEnqueued());

        //finalizeが呼ばれてカウントダウンされるのを待つ
        //いつまで経っても終わらないと困るので
        //3秒待ってダメなら失敗と見なす
        finalized.await(3, TimeUnit.SECONDS);
    }
}
