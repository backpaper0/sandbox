package example;

import static org.hamcrest.CoreMatchers.isA;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ExceptionTest {

    @EJB
    private EjbBean ejbBean;

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Rule
    public ExpectedException ee = ExpectedException.none();

    @Test
    public void test_EJB_NoResultException() throws Exception {
        ee.expect(EJBException.class);
        ee.expectCause(isA(NoResultException.class));
        ejbBean.noResultException();
    }

    @Test
    public void test_EJB_OptimisticLockException() throws Exception {
        ee.expect(EJBException.class);
        ee.expectCause(isA(OptimisticLockException.class));
        ejbBean.optimisticLockException();
    }

    @Before
    public void setUp() throws Exception {
        utx.begin();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<ExampleEntity> q = cb
                .createCriteriaDelete(ExampleEntity.class);
        em.createQuery(q).executeUpdate();

        ExampleEntity entity1 = new ExampleEntity();
        entity1.setId(1);
        em.persist(entity1);

        ExampleEntity entity2 = new ExampleEntity();
        entity2.setId(2);
        entity2.setVersion(2);
        em.persist(entity2);

        utx.commit();
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(EjbBean.class, ExampleEntity.class,
                        ExampleEntity_.class)
                .addAsResource("META-INF/persistence.xml");
    }
}
