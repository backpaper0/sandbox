package sample;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class BasicAuthTest extends JerseyTest {

    @Test
    public void ok() throws Exception {
        Response resp = target("hello")
                .register(
                        HttpAuthenticationFeature.basic("backpaper0", "secret"))
                .request().get();
        assertThat(resp.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(resp.readEntity(String.class), is("Hello, world!"));
    }

    @Test
    public void unauthorized() throws Exception {
        Response resp = target("hello").request().get();
        assertThat(resp.getStatus(), is(Status.UNAUTHORIZED.getStatusCode()));
        assertThat(resp.readEntity(String.class), is("(´;ω;`)"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloResource.class, BasicAuthFilter.class);
    }
}