package example;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

public class App {

    public static void main(String[] args) throws Exception {

        PathHandler path = Handlers.path();

        DeploymentInfo deployment = Servlets.deployment().setClassLoader(App.class.getClassLoader())
                .setContextPath("/")

                //JAX-RS(Resteasy)を使う設定
                .addServlet(Servlets.servlet(HttpServlet30Dispatcher.class).setAsyncSupported(true)
                        .setLoadOnStartup(1).addMapping("/api/*")
                        .addInitParam(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX,
                                "/api")
                        .addInitParam("javax.ws.rs.Application", JaxrsActivator.class.getName()))

                //WebSocketを使う設定
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
                        new WebSocketDeploymentInfo().addEndpoint(EchoServer.class))

                .setDeploymentName("echo.war");

        ServletContainer container = Servlets.defaultContainer();

        DeploymentManager manager = container.addDeployment(deployment);
        manager.deploy();
        path.addPrefixPath(deployment.getContextPath(), manager.start());

        Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(path)
                .build();
        server.start();
    }

    @ServerEndpoint("/echo")
    public static class EchoServer {

        @OnMessage
        public void handle(String text, Session session) {
            session.getAsyncRemote().sendText(text);
        }
    }

    @Path("echo")
    public static class EchoResource {

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.TEXT_PLAIN)
        public String post(@FormParam("text") String text) {
            return text;
        }
    }

    @ApplicationPath("api")
    public static class JaxrsActivator extends Application {

        @Override
        public Set<Class<?>> getClasses() {
            return Stream.of(EchoResource.class).collect(Collectors.toSet());
        }
    }
}