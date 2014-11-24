package net.hogedriven.jersey.guice;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.BindingBuilderFactory;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.glassfish.jersey.server.spi.ComponentProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceComponentProvider implements ComponentProvider {

    private static Logger logger = Logger.getLogger(GuiceComponentProvider.class.getName());

    private ServiceLocator locator;

    private Injector injector;

    @Override
    public void initialize(ServiceLocator locator) {
        this.locator = locator;
        Module module = locator.getService(Module.class);
        this.injector = Guice.createInjector(module);
    }

    @Override
    public boolean bind(Class<?> component, Set<Class<?>> providerContracts) {
        
        if (component.getPackage().getName().startsWith("echo") == false) {
            return false;
        }

        if (logger.isLoggable(Level.CONFIG)) {
            logger.log(Level.CONFIG, "Bound: {0}", new Object[] { component });
        }

        DynamicConfiguration configuration = ServiceLocatorUtilities.createDynamicConfiguration(locator);
        ServiceBindingBuilder<Object> builder = BindingBuilderFactory.newFactoryBinder(new GuiceFactory(component));
        builder.to(component);
        for (Class<?> providerContract : providerContracts) {
            builder.to(providerContract);
        }
        BindingBuilderFactory.addBinding(builder, configuration);
        configuration.commit();
        return true;
    }

    @Override
    public void done() {
    }

    private class GuiceFactory implements Factory<Object> {

        private final Class<?> component;

        public GuiceFactory(Class<?> component) {
            this.component = component;
        }

        @Override
        public Object provide() {
            return injector.getInstance(component);
        }

        @Override
        public void dispose(Object instance) {
        }
    }
}
