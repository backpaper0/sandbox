package sample;

import javax.enterprise.context.*;
import javax.persistence.*;
import java.util.*;

@ApplicationScoped
@javax.transaction.Transactional
public class SampleService {
    @PersistenceContext
    private EntityManager em;
    public List<Sample> list() {
        return em.createQuery("FROM Sample a", Sample.class).getResultList();
    }
    public Optional<Sample> get(Integer id) {
        return Optional.ofNullable(em.find(Sample.class, id));
    }
    public Sample create(String text) {
        Sample entity = new Sample();
        entity.setText(text);
        em.persist(entity);
        return entity;
    }
}
