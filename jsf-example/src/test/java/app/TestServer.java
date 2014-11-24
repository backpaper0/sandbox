package app;

import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.catalina.startup.Tomcat;

public class TestServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("target/tomcat");
        tomcat.setPort(8080);

        tomcat.addWebapp("/", Paths.get("src/main/webapp").toAbsolutePath()
                .toString());

        tomcat.start();

        SwingUtilities.invokeAndWait(() -> JOptionPane.showMessageDialog(null,
                "STARTED"));

        tomcat.stop();
    }
}
