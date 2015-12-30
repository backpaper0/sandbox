package sample.bar;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * インターセプターが適用されないCDI管理ビーン。
 * 
 */
@RequestScoped
@Path("bar")
public class BarApi {

    @GET
    public String get() {
        return "bar";
    }
}
