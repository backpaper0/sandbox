package echo.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import net.hogedriven.jersey.guice.GuiceComponentProvider;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.filter.ExcludeRegExpPaths;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import echo.EchoApplication;

@RunWith(Arquillian.class)
public class EchoIT {

    @Test
    public void testEcho() throws Exception {
        Client client = ClientBuilder.newClient();
        String response = client
                .target("http://localhost:8080/app/rest/echo")
                .request()
                .post(Entity.text("hello"), String.class);
        assertThat(response, is("hello"));
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        File[] jars = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        ExcludeRegExpPaths filter = new ExcludeRegExpPaths("^.+Test(\\$.+)?\\.class$");

        WebArchive war = ShrinkWrap.create(WebArchive.class, "app.war")
                .addPackages(true, filter, EchoApplication.class.getPackage())
                .addPackages(true, filter, GuiceComponentProvider.class.getPackage())
                .addAsResource("META-INF/services/org.glassfish.jersey.server.spi.ComponentProvider")
                .addAsLibraries(jars);

        Logger.getGlobal().info(war.toString(true));

        return war;
    }
}