package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloWorldTest extends JerseyTest {

    /**
     * nameを設定しているのでリソースメソッドには
     * Optional.of("backpaper0")が渡される。
     * @throws Exception
     */
    @Test
    public void testHelloWithName() throws Exception {
        String said = target("hello").queryParam("name", "backpaper0")
                .request().get(String.class);
        assertThat(said, is("Hello, backpaper0!"));
    }

    /**
     * nameを設定していないのでリソースメソッドには
     * Optional.empty()が渡される。
     * @throws Exception
     */
    @Test
    public void testHelloDefaultName() throws Exception {
        String said = target("hello").request().get(String.class);
        assertThat(said, is("Hello, world!"));
    }

    @Override
    protected Application configure() {
        //リソースクラスとOptionalに変換するための
        //ParamConverterProviderを渡す。
        return new ResourceConfig().register(HelloWorld.class).register(
                OptionalParamConverterProvider.class);
    }
}
