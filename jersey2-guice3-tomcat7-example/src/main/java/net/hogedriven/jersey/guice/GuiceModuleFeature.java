package net.hogedriven.jersey.guice;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.google.inject.Module;

public class GuiceModuleFeature implements Feature {

    private final Class<? extends Module> moduleClass;

    public GuiceModuleFeature(Class<? extends Module> moduleClass) {
        this.moduleClass = moduleClass;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new GuiceBinder());
        return true;
    }

    class GuiceBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(moduleClass).to(Module.class).in(Singleton.class);
        }
    }
}
