package app;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class CalcTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        int c = target("calc/add").queryParam("a", 2).queryParam("b", 3)
                .request().get(int.class);
        assertThat(c, is(5));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Calc.class);
    }
}
