package jersey;

import java.io.OutputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;

public class JerseyHandler implements HttpHandler {

    private ApplicationHandler applicationHandler;

    private Executor executor;

    public JerseyHandler(Application application) {
        applicationHandler = new ApplicationHandler(application);
        executor = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.dispatch(executor, () -> {
            exchange.startBlocking();

            URI baseUri = null;
            URI requestUri = URI.create(
                    exchange.getRequestURI() + "?" + exchange.getQueryString());
            String httpMethod = exchange.getRequestMethod().toString();
            SecurityContext securityContext = new UndertowSecurityContext();
            PropertiesDelegate propertiesDelegate = new UndertowPropertiesDelegate();
            ContainerRequest request = new ContainerRequest(baseUri, requestUri,
                    httpMethod, securityContext, propertiesDelegate);

            request.setEntityStream(exchange.getInputStream());

            for (HeaderValues header : exchange.getRequestHeaders()) {
                request.headers(header.getHeaderName().toString(), header);
            }

            ContainerResponseWriter writer = new UndertowResponseWriter(
                    exchange);
            request.setWriter(writer);

            applicationHandler.handle(request);
        });
    }

    private static class UndertowPropertiesDelegate
            implements PropertiesDelegate {

        @Override
        public Object getProperty(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Collection<String> getPropertyNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setProperty(String name, Object object) {
            // TODO Auto-generated method stub
        }

        @Override
        public void removeProperty(String name) {
            // TODO Auto-generated method stub
        }
    }

    private static class UndertowSecurityContext implements SecurityContext {

        @Override
        public Principal getUserPrincipal() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isSecure() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class UndertowResponseWriter
            implements ContainerResponseWriter {

        private final HttpServerExchange exchange;

        public UndertowResponseWriter(HttpServerExchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public OutputStream writeResponseStatusAndHeaders(long contentLength,
                ContainerResponse responseContext) throws ContainerException {
            exchange.setStatusCode(responseContext.getStatus());
            exchange.setResponseContentLength(contentLength);
            for (String name : responseContext.getHeaders().keySet()) {
                exchange.getResponseHeaders().put(
                        HttpString.tryFromString(name),
                        responseContext.getHeaders().getFirst(name).toString());
            }
            return exchange.getOutputStream();
        }

        @Override
        public boolean suspend(long timeOut, TimeUnit timeUnit,
                TimeoutHandler timeoutHandler) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setSuspendTimeout(long timeOut, TimeUnit timeUnit)
                throws IllegalStateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void commit() {
            // TODO Auto-generated method stub
        }

        @Override
        public void failure(Throwable error) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean enableResponseBuffering() {
            // TODO Auto-generated method stub
            return false;
        }
    }
}
