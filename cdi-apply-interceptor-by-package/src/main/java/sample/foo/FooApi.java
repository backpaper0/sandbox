package sample.foo;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * インターセプターが適用されるCDI管理ビーン。
 *
 */
@RequestScoped
@Path("foo")
public class FooApi {

    @GET
    public String get() {
        return "foo";
    }
}
