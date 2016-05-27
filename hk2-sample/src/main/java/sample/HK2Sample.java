package sample;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class HK2Sample {

    public static void main(String[] args) {

        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        ServiceLocatorUtilities.bind(locator, new AbstractBinder() {

            @Override
            protected void configure() {
                bind(HelloImpl.class).to(Hello.class).in(Singleton.class).proxy(true);
            }
        });
        HK2Sample sample = new HK2Sample();
        locator.inject(sample);

        System.out.println(sample.hello.getClass());
        System.out.println(sample.hello.say());
    }

    @Inject
    private Hello hello;

    public interface Hello {
        String say();
    }

    public static class HelloImpl implements Hello {
        @Override
        public String say() {
            return "Hello, world!";
        }
    }
}
