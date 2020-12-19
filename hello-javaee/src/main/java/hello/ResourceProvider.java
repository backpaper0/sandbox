package hello;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@Dependent
public class ResourceProvider {

    @PersistenceContext(unitName = "defaultUnit")
    private EntityManager em;

    @PersistenceUnit(unitName = "defaultUnit")
    private EntityManagerFactory emf;

    @Resource(name = "java:comp/DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return em;
    }

    @Produces
    @Dependent
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @Produces
    @ApplicationScoped
    public ManagedExecutorService getExecutor() {
        return executor;
    }
}
