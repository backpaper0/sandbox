package sample;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("hello")
@LocalBean
@Stateless
public class Hello {

    @Inject
    private MessageDao messageDao;

    @GET
    @Template("hello.html")
    public HelloResponse sayHello(@QueryParam("yourName") String yourName) {
        HelloResponse response = new HelloResponse();
        Message message = messageDao.select(1L);
        response.setMessage(String.format(message.template, yourName));
        return response;
    }

    public static class HelloResponse {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
