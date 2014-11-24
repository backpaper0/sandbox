package echo;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import net.hogedriven.jersey.guice.GuiceModuleFeature;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class EchoResourceTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        String resp = target("echo").request().post(Entity.text("hello"),
                String.class);
        assertThat(resp, is("hello"));
    }

    @Override
    protected Application configure() {
        ResourceConfig app = new ResourceConfig();
        app.register(EchoResource.class);
        app.register(new GuiceModuleFeature(EchoModule.class));
        return app;
    }
}
