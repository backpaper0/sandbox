

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class ReferenceTest {

    @Test
    public void test_WeakReference() throws Exception {

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

        assertThat(ref.isEnqueued(), is(false));

        System.gc();

        assertThat(ref.isEnqueued(), is(true));

        //finalizeが呼ばれてカウントダウンされるのを待つ
        //いつまで経っても終わらないと困るので
        //3秒待ってダメなら失敗と見なす
        finalized.await(3, TimeUnit.SECONDS);
    }
}
