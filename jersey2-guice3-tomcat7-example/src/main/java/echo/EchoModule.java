package echo;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Context;

import org.glassfish.hk2.api.ServiceLocator;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class EchoModule extends AbstractModule {

    @Inject
    private ServiceLocator locator;

    @Override
    protected void configure() {
        bind(EchoResource.class);
        bind(Echo.class).to(EchoImpl.class);

        bindListener(Matchers.any(), new AtContextTypeListener());
    }

    class AtContextTypeListener implements TypeListener {

        Logger logger = Logger.getLogger(getClass().getName());

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            for (Class<?> c = type.getRawType(); c != null; c = c
                    .getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Context.class)) {
                        encounter.register(new AtContextMembersInjector(field));

                        if (logger.isLoggable(Level.CONFIG)) {
                            logger.log(Level.CONFIG, "Registered: {0}",
                                    new Object[] { field });
                        }
                    }
                }
            }
        }
    }

    class AtContextMembersInjector implements MembersInjector<Object> {

        private final Field field;

        public AtContextMembersInjector(Field field) {
            this.field = field;
        }

        @Override
        public void injectMembers(Object instance) {
            if (field.isAccessible() == false) {
                field.setAccessible(true);
            }

            Object value = locator.getService(field.getGenericType());

            try {
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
