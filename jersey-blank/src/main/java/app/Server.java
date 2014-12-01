package app;

import java.io.IOException;
import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws IOException {
        URI uri = URI.create("http://localhost:8080/rest/");

        ResourceConfig rc = new ResourceConfig();
        rc.register(Calc.class);

        HttpServer httpServer = JdkHttpServerFactory.createHttpServer(uri, rc);

        System.out.println("JAX-RS started");
        System.in.read();

        httpServer.stop(0);
    }
}
