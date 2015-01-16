package example;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;

import com.sun.net.httpserver.HttpServer;

public class App {

    public static void main(String[] args) {
        Weld weld = new Weld();
        weld.initialize();
        URI uri = URI.create("http://localhost:8080/");
        ResourceConfig config = new ResourceConfig(HelloResource.class);
        HttpServer server = JdkHttpServerFactory.createHttpServer(uri, config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(0);
            weld.shutdown();
        }));

        System.out.println("http://localhost:8080/hello?name=YourName");
    }
}
