package sample;

import java.nio.file.Paths;

import org.apache.catalina.startup.Tomcat;

public class Server {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.addWebapp("/", Paths.get("src/main/webapp").toAbsolutePath()
                .toString());
        tomcat.start();
        System.out.println("http://localhost:8080/rest/hello");
        System.in.read();
        tomcat.stop();
    }
}
