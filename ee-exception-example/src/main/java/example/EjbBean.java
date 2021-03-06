package example;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class EjbBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private EjbBean2 ejbBean2;

    public void noResultException() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExampleEntity> q = cb.createQuery(ExampleEntity.class);
        Root<ExampleEntity> from = q.from(ExampleEntity.class);
        q.where(cb.equal(from.get(ExampleEntity_.id), -1));
        em.createQuery(q).getSingleResult();
    }

    public void optimisticLockException() {
        ExampleEntity entity = new ExampleEntity();
        entity.setId(2);
        entity.setVersion(1);
        em.merge(entity);
    }

    public void exception() throws ExampleException {
        ejbBean2.exception();
    }

    public void runtimeException() {
        ejbBean2.runtimeException();
    }
}
