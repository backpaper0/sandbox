package sample;

import com.sun.jersey.api.client.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import static org.hamcrest.CoreMatchers.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JaxrsThymeleafTest {

    @Resource(name = "jdbc/__default")
    private DataSource dataSource;

    @Test
    public void test() throws Exception {
        String actual =
                Client
                .create()
                .resource("http://localhost:8181/test/resource/hello")
                .queryParam("yourName", "world")
                .get(String.class);
        String expected =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
                + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n"
                + "<title>hello</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p>Hello, world!</p>\n"
                + "</body>\n"
                + "</html>\n";
        assertThat(actual, is(expected));
    }

    @Before
    public void setUp() throws Exception {
        try (Connection con = dataSource.getConnection()) {
            try (Statement st = con.createStatement()) {
                st.execute("CREATE TABLE message (id BIGINT primary key, template VARCHAR(100))");
            }
            try (PreparedStatement pst = con.prepareStatement("INSERT INTO message (id, template) VALUES (?, ?)")) {
                pst.setLong(1, 1L);
                pst.setString(2, "Hello, %s!");
                pst.executeUpdate();
            }
        }
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "sample")
                .addAsResource("META-INF/sample/MessageDao/select.sql")
                .addAsResource("META-INF/services/javax.ws.rs.ext.MessageBodyWriter")
                .addAsResource("hello.html")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
