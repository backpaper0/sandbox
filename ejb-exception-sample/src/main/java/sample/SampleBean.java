package sample;

import java.lang.reflect.Field;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("")
public class SampleBean {

    @Resource
    private TransactionSynchronizationRegistry reg;

    private void register(String path) {
        reg.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {
            }
            @Override
            public void afterCompletion(int status) {
                try {
                    for (Field field : Status.class.getDeclaredFields()) {
                        int i = (int) field.get(null);
                        if (i == status) {
                            System.out.printf("******** %s:%s ********%n", path, field.getName());
                        }
                    }
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        });
    }

    @Path("1")
    @GET
    public String post1() throws Exception {
        register("1");
        throw new Exception();
    }

    @Path("2")
    @GET
    public String post2() {
        register("2");
        throw new RuntimeException();
    }

    @Path("3")
    @GET
    public String post3() throws SampleException {
        register("3");
        throw new SampleException();
    }

    @Path("4")
    @GET
    public String post4() {
        register("4");
        throw new SampleRuntimeException();
    }

    @Path("5")
    @GET
    public String post5() {
        register("5");
        throw new SampleRuntimeException2();
    }

    @Path("6")
    @GET
    public String post6() {
        register("6");
        throw new SampleRuntimeException3();
    }
}
