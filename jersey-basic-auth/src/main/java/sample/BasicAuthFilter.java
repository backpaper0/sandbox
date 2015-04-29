package sample;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

    String username = "backpaper0";
    String password = "secret";

    @Override
    public void filter(ContainerRequestContext rc) throws IOException {
        String auth = rc.getHeaderString(HttpHeaders.AUTHORIZATION);

        Runnable abort = () -> rc.abortWith(Response
                .status(Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,
                        "Basic realm=\"Enter username and password.\"")
                .entity("(´;ω;`)").type(MediaType.TEXT_PLAIN_TYPE).build());

        if (auth == null || auth.startsWith("Basic ") == false) {
            abort.run();
            return;
        }

        String usernameAndPassword = new String(Base64.getDecoder().decode(
                auth.substring("Basic ".length())), StandardCharsets.UTF_8);

        if (usernameAndPassword.equals(username + ":" + password) == false) {
            abort.run();
            return;
        }
    }
}
