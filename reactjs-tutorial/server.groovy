@Grab('org.glassfish.jersey.containers:jersey-container-grizzly2-http:2.23.2')
import groovy.json.*
import java.net.*
import javax.ws.rs.*
import org.glassfish.jersey.grizzly2.httpserver.*
import org.glassfish.jersey.server.*

@Path('/')
class Controller {

    static data = []

    @GET
    @Produces('text/html')
    String index() {
        new File('index.html').text
    }

    @Path('api/comments')
    @GET
    @Produces('application/json')
    String comments() {
        def json = new JsonBuilder()
        json(data)
        json.toString()
    }

    @Path('api/comments')
    @POST
    @Consumes('application/x-www-form-urlencoded')
    void handleComment(
            @FormParam('author') String author,
            @FormParam('text') String text) {
        data += ['author':author,'text':text]
    }
}

def server = GrizzlyHttpServerFactory.createHttpServer(
    URI.create('http://localhost:8080/'),
    new ResourceConfig(Controller.class)
)

