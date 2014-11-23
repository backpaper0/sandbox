package param;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("ParamExample")
public class ParamExample {

    @Path("string")
    @GET
    public String string(@QueryParam("in") String in) {
        return "string: " + in;
    }

    @Path("primitiveInt")
    @GET
    public String primitiveInt(@QueryParam("in") int in) {
        return "primitiveInt: " + String.valueOf(in);
    }

    @Path("constructor")
    @GET
    public String constructor(@QueryParam("in") PublicConstructor in) {
        return "constructor: " + in.getValue();
    }

    @Path("valueOf")
    @GET
    public String valueOf(@QueryParam("in") ValueOf in) {
        return "valueOf: " + in.getValue();
    }

    @Path("fromString")
    @GET
    public String fromString(@QueryParam("in") FromString in) {
        return "fromString: " + in.getValue();
    }

    @Path("valueOfAndFromString")
    @GET
    public String valueOfAndFromString(@QueryParam("in") ValueOfAndFromString in) {
        return "valueOfAndFromString: " + in.getValue();
    }

    @Path("enumFromString")
    @GET
    public String enumFromString(@QueryParam("in") EnumFromString in) {
        return "enumFromString: " + in.getValue();
    }

    @Path("converter")
    @GET
    public String converter(@QueryParam("in") ParamConverted in) {
        return "converter: " + in.getValue();
    }
}
