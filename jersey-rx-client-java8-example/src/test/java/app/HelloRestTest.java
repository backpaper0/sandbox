package app;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.client.rx.java8.RxCompletionStage;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloRestTest extends JerseyTest {

    @Test
    public void testRxJava8() throws Exception {
        CompletionStage<String> stage = RxCompletionStage.from(target("hello"))
                .queryParam("name", "world").request().rx().get(String.class);

        CompletionStage<Void> stage2 = stage.thenAccept(s -> assertThat(s,
                is("Hello, world!")));

        //終了を待つためアレ
        stage2.toCompletableFuture().get();
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
