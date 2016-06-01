package sample;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("accounts")
public class AccountApi {

    @Inject
    private AccountService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> all() {
        return service.findAll();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account get(@PathParam("id") Long id) {
        return service.findBy(id);
    }

    @Path("new")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Account create(@FormParam("name") String name) {
        return service.create(name);

    }

    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Account update(@PathParam("id") Long id, @FormParam("name") String name) {
        return service.update(id, name);
    }

    @Path("{id}:delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
}
