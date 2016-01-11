package reactivestreams;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.rx.Streams;

public class ReactorStudy {

    @Test
    public void test() throws Exception {

        Publisher<String> p = Streams.just("foo", "bar", "baz");

        List<String> list = new ArrayList<>();

        p.subscribe(new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(3);
            }

            @Override
            public void onNext(String t) {
                list.add(t);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
                list.add("completed");
            }
        });

        assertThat(list).containsOnly("foo", "bar", "baz", "completed");
    }

}
