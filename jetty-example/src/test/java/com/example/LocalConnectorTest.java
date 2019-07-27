package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.jetty.http.HttpTester;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;

class LocalConnectorTest {

    @Test
    void test() throws Exception {
        final Server server = new Server();
        final LocalConnector localConnector = new LocalConnector(server);
        server.addConnector(localConnector);
        final ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/demo");
        handler.addServlet(HelloServlet.class, "/hello");
        server.setHandler(handler);
        server.start();

        final HttpTester.Request request = HttpTester.newRequest();
        request.setMethod("GET");
        request.setURI("/demo/hello");
        request.setVersion(HttpVersion.HTTP_1_1);
        request.setHeader("Host", "example.com");
        final String requestText = request.toString();
        System.out.println(requestText);

        final String responseText = localConnector.getResponse(requestText);
        System.out.println(responseText);

        final HttpTester.Response response = HttpTester.parseResponse(responseText);

        assertEquals(200, response.getStatus());
        assertEquals("Hello, world!", response.getContent());
    }
}
