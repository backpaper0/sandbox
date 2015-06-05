package hello;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
@Stateless
public class HelloEjb {

    @Inject
    private EntityManager em;

    public String get() {

        HelloJpa entity = new HelloJpa();
        entity.setValue("Hello, JPA!");
        em.persist(entity);

        return "Hello, EJB!";
    }
}
