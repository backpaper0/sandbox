package sample;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

public class HelloServiceExtension implements Extension {

    public void addHelloService(@Observes final BeforeBeanDiscovery event, final BeanManager bm) {
        final AnnotatedType<HelloService> type = bm.createAnnotatedType(HelloService.class);
        event.addAnnotatedType(new AnnotatedTypeImpl(type));
    }

    public static class AnnotatedTypeImpl implements AnnotatedType<HelloService> {

        private final AnnotatedType<HelloService> type;

        public AnnotatedTypeImpl(final AnnotatedType<HelloService> type) {
            this.type = type;
        }

        @Override
        public Class<HelloService> getJavaClass() {
            return type.getJavaClass();
        }

        @Override
        public Type getBaseType() {
            return type.getBaseType();
        }

        @Override
        public Set<AnnotatedConstructor<HelloService>> getConstructors() {
            return type.getConstructors();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return type.getTypeClosure();
        }

        @Override
        public Set<AnnotatedMethod<? super HelloService>> getMethods() {
            final Set<AnnotatedMethod<? super HelloService>> methods = Collections
                    .newSetFromMap(new IdentityHashMap<>());
            for (final AnnotatedMethod<? super HelloService> method : type.getMethods()) {
                if (method.getJavaMember().getName().equals("afterPropertiesSet")) {
                    methods.add(new AnnotatedMethodImpl(method));
                } else {
                    methods.add(method);
                }
            }
            return methods;
        }

        @Override
        public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
            return type.getAnnotation(annotationType);
        }

        @Override
        public Set<AnnotatedField<? super HelloService>> getFields() {
            return type.getFields();
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return type.getAnnotations();
        }

        @Override
        public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
            return type.isAnnotationPresent(annotationType);
        }
    }

    public static class AnnotatedMethodImpl implements AnnotatedMethod<HelloService> {

        private final AnnotatedMethod<HelloService> method;

        public AnnotatedMethodImpl(final AnnotatedMethod<? super HelloService> method) {
            this.method = (AnnotatedMethod<HelloService>) method;
        }

        @Override
        public List<AnnotatedParameter<HelloService>> getParameters() {
            return method.getParameters();
        }

        @Override
        public boolean isStatic() {
            return method.isStatic();
        }

        @Override
        public AnnotatedType<HelloService> getDeclaringType() {
            return method.getDeclaringType();
        }

        @Override
        public Type getBaseType() {
            return method.getBaseType();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return method.getTypeClosure();
        }

        @Override
        public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
            if (annotationType == PostConstruct.class) {
                return (T) new PostConstructImpl();
            }
            return method.getAnnotation(annotationType);
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return Collections.singleton(new PostConstructImpl());
        }

        @Override
        public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
            return annotationType == PostConstruct.class;
        }

        @Override
        public Method getJavaMember() {
            return method.getJavaMember();
        }
    }

    public static class PostConstructImpl extends AnnotationLiteral<PostConstruct>
            implements PostConstruct {
    }
}
