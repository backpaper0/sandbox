package sample;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class EntityManagerProducer {

    @PersistenceContext
    private EntityManager em;

    @Produces
    public EntityManager entityManager() {
        return em;
    }
}