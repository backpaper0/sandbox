package app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import rx.Observable;

public class HelloRestTest extends JerseyTest {

    @Test
    public void testRxJava8() throws Exception {
        RxClient<RxCompletionStageInvoker> client = Rx.from(getClient(),
                RxCompletionStageInvoker.class);
        CompletionStage<String> stage = client.target(getBaseUri())
                .path("hello").queryParam("name", "world").request().rx()
                .get(String.class);

        String s = stage.toCompletableFuture().get();
        assertThat(s, is("Hello, world!"));
    }

    @Test
    public void testRxRxJava() throws Exception {
        RxClient<RxObservableInvoker> client = Rx.from(getClient(),
                RxObservableInvoker.class);
        Observable<String> obs = client.target(getBaseUri()).path("hello")
                .queryParam("name", "world").request().rx().get(String.class);

        String s = obs.toBlocking().first();
        assertThat(s, is("Hello, world!"));
    }

    @Test
    public void test() throws Exception {
        String resp = target("hello").queryParam("name", "world").request()
                .get(String.class);

        assertThat(resp, is("Hello, world!"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig().register(HelloRest.class);
    }
}
