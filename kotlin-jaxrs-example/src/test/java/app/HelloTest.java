package app;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Arquillian.class)
public class HelloTest {

    @ArquillianResource
    private URI uri;

    @Test
    @RunAsClient
    public void testHello() throws Exception {

        String response = ClientBuilder.newClient()
                .target(uri)
                .path("rest/hello")
                .queryParam("name", "world")
                .request()
                .get(String.class);

        assertThat(response, is("Hello, world!"));
    }

    @Deployment
    public static WebArchive createDeployment() {

        File[] jars = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class, "app.war")
                .addClasses(App.class, Hello.class)
                .addAsLibraries(jars);

        return war;
    }
}
