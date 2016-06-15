package sample;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SampleService {

    @Inject
    private EntityManager em;

    public Optional<Sample> get(Long id) {
        return Optional.ofNullable(em.find(Sample.class, id));
    }

    public List<Sample> findAll() {
        return em.createNamedQuery("Sample.findAll", Sample.class).getResultList();
    }

    public Sample create(String text) {
        Sample entity = new Sample();
        entity.setText(text);
        em.persist(entity);
        return entity;
    }

    public Optional<Sample> update(Long id, String text) {
        Sample entity = em.find(Sample.class, id);
        if (entity == null) {
            return Optional.empty();
        }
        entity.setText(text);
        return Optional.of(entity);
    }
}
