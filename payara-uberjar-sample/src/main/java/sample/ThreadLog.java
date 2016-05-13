package sample;

import javax.ws.rs.container.*;

@javax.ws.rs.ext.Provider
public class ThreadLog implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws java.io.IOException {
        response.getHeaders().putSingle("X-Thread-Name", Thread.currentThread().getName());
    }
}
