package sample;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.UnaryOperator;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Transactional
@Path("")
public class SampleResource {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return em.createQuery("FROM Sample s", Sample.class).getResultList().stream().findFirst()
                .map(Object::getClass).map(c -> {
                    StringWriter s = new StringWriter();
                    UnaryOperator<String> f = a -> "    " + a;
                    try (PrintWriter out = new PrintWriter(s)) {
                        out.println("class");
                        out.println(f.apply(c.getName()));
                        out.println("fields");
                        Arrays.stream(c.getDeclaredFields()).map(Field::toGenericString).map(f)
                                .forEach(out::println);
                        out.println("methods");
                        Arrays.stream(c.getDeclaredMethods()).map(Method::toGenericString).map(f)
                                .forEach(out::println);
                    }
                    return s.toString();
                }).orElse("<empty>");
    }

    @POST
    public void post(String text) {
        Sample entity = new Sample();
        entity.setText(text);
        em.persist(entity);
    }
}
