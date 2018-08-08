package reactivestreams;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

public class ReactorStudy {

    @Test
    public void test() throws Exception {

        final Publisher<String> p = Flux.just("foo", "bar", "baz");

        final List<String> list = new ArrayList<>();

        p.subscribe(new Subscriber<String>() {

            @Override
            public void onSubscribe(final Subscription s) {
                s.request(3);
            }

            @Override
            public void onNext(final String t) {
                list.add(t);
            }

            @Override
            public void onError(final Throwable t) {
            }

            @Override
            public void onComplete() {
                list.add("completed");
            }
        });

        assertThat(list).containsOnly("foo", "bar", "baz", "completed");
    }

}
