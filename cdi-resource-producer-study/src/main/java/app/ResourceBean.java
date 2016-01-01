package app;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Stateless
public class ResourceBean {

    @PersistenceContext
    private EntityManager em;
    @Resource
    private ManagedExecutorService executor;
    @Resource
    private DataSource dataSource;

    public String get() {
        return Stream.of(this, em, executor, dataSource)
                .map(a -> a.getClass().getName() + "@"
                        + System.identityHashCode(a))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
