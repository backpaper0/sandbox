import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.catalina.startup.Tomcat;

public class DevServer {

    public static void main(String[] args) throws Exception {

        final String contextPath = "/";
        final int port = 8080;

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(Paths.get("target/tomcat").toAbsolutePath()
                .toString());
        tomcat.setPort(port);
        tomcat.enableNaming();

        tomcat.addWebapp(contextPath, Paths.get("src/main/webapp")
                .toAbsolutePath().toString());

        tomcat.start();

        try (Scanner in = new Scanner(System.in)) {
            in.nextLine();
        }

        tomcat.stop();
    }
}
