package experiment;

import java.io.File;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EchoBeanTest {

    @Inject
    private EchoBean bean;

    @EJB
    private EchoBean2 bean2;
    @EJB
    private EchoBean3 bean3;

    //    @Resource
    //    private UserTransaction utx;

    @Test
    public void test0_デプロイしたりJIT見込んで温めるやつ() throws Exception {
        //WildFlyの起動やデプロイの時間を分離したいので
        //それとJIT効くかもしれないし温める
        for (int i = 0; i < 100000; i++) {
            bean.echo(String.valueOf(i));
            bean.echoInTx(String.valueOf(i));
            bean2.echo(String.valueOf(i));
            bean3.echo(String.valueOf(i));
        }
    }

    @Test
    public void test3_ApplicationScopedなCDIでメソッドにTransactionalつけた()
            throws Exception {
        for (int i = 0; i < 100000; i++) {
            bean.echoInTx(String.valueOf(i));
        }
    }

    @Test
    public void test4_ApplicationScopedなCDIでTransactional無し() throws Exception {
        for (int i = 0; i < 100000; i++) {
            bean.echo(String.valueOf(i));
        }
    }

    @Test
    public void test1_Statlessセッションビーン() throws Exception {
        for (int i = 0; i < 100000; i++) {
            bean2.echo(String.valueOf(i));
        }
    }

    @Test
    public void test2_Singletonセッションビーン() throws Exception {
        for (int i = 0; i < 100000; i++) {
            bean3.echo(String.valueOf(i));
        }
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(EchoBean.class, EchoBean2.class, EchoBean3.class)
                .addAsWebInfResource(
                        new File("src/main/webapp/WEB-INF/glassfish-web.xml"));
    }
}
