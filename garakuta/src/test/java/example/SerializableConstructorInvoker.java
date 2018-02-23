package example;

import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class SerializableConstructorInvoker {

    private static final Method factory;

    static {
        try {
            factory = ObjectStreamClass.class.getDeclaredMethod(
                    "getSerializableConstructor", Class.class);
            if (factory.isAccessible() == false) {
                factory.setAccessible(true);
            }
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(final Class<T> clazz) {
        try {
            final Constructor<T> constructor = castConstructor(factory.invoke(null, clazz));
            return constructor.newInstance();
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> castConstructor(final Object o) {
        return (Constructor<T>) o;
    }
}
