package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ReferenceTest {

    @Test
    public void test_WeakReference() throws Exception {

        //finalizeが呼ばれることを確認するために使用するラッチ
        final CountDownLatch finalized = new CountDownLatch(1);

        //assertのために一旦変数に突っ込む
        Date date = new Date() {

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                finalized.countDown();
            }
        };

        /*
         * 弱い参照にする
         * ソフト参照だとこの規模だとGCされなかった
         */
        WeakReference<Date> ref = new WeakReference<Date>(date);

        assertThat(ref.get(), sameInstance(date));

        //強い参照があるとGCされないのでnullを
        //突っ込んで強い参照を断ち切る
        date = null;

        gc();

        assertThat(ref.get(), nullValue());

        //finalizeが呼ばれてカウントダウンされるのを待つ
        //いつまで経っても終わらないと困るので
        //3秒待ってダメなら失敗と見なす
        finalized.await(3, TimeUnit.SECONDS);
    }

    //free memoryが増えなくなるまでGCする
    static void gc() {
        long beforeFreeMemory = Runtime.getRuntime().freeMemory();
        while (true) {
            System.gc();
            long freeMemory = Runtime.getRuntime().freeMemory();
            if (beforeFreeMemory == freeMemory) {
                break;
            }
            beforeFreeMemory = freeMemory;
        }
    }
}