package sample;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.internal.StarFilter;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class InterceptorSample {

    public static void main(String[] args) {
        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        ServiceLocatorUtilities.bind(locator, new AbstractBinder() {

            @Override
            protected void configure() {

                //must be in the singleton scope
                bind(InterceptionServiceImpl.class).to(InterceptionService.class)
                        .in(Singleton.class);

                bind(Hello.class).to(Hello.class);
            }
        });
        Hello service = locator.getService(Hello.class);
        System.out.println(service.say());
        System.out.println(service.getClass());
    }

    public static class Hello {
        public String say() {
            return "hello";
        }
    }

    public static class InterceptionServiceImpl implements InterceptionService {

        @Override
        public Filter getDescriptorFilter() {
            return StarFilter.getDescriptorFilter();
        }

        @Override
        public List<MethodInterceptor> getMethodInterceptors(Method method) {
            return Collections.singletonList(i -> "*" + i.proceed() + "*");
        }

        @Override
        public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
            return Collections.emptyList();
        }
    }
}
