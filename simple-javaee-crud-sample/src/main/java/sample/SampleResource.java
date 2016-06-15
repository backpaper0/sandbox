package sample;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("sample")
public class SampleResource {

    @Inject
    private SampleService service;

    @Path("{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@PathParam("id") Long id) {
        return service.get(id).map(Objects::toString).orElseThrow(NotFoundException::new);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String findAll() {
        return service.findAll().stream().map(Objects::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@FormParam("text") String text) {
        return service.create(text).toString();
    }

    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@PathParam("id") Long id, @FormParam("text") String text) {
        return service.update(id, text).map(Objects::toString).orElseThrow(NotFoundException::new);
    }
}
