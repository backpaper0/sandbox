package example.javacompiler;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InMemoryClassLoader extends ClassLoader {

    private final Map<String, ByteArrayOutputStream> classes = new HashMap<>();

    public void addClass(final String className, final ByteArrayOutputStream out) {
        classes.put(className, out);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {

        Class<?> clazz = findLoadedClass(name);
        if (clazz != null) {
            return clazz;
        }

        final ByteArrayOutputStream out = classes.get(name);
        if (out == null) {
            return super.loadClass(name, resolve);
        }

        final byte[] b = out.toByteArray();

        clazz = defineClass(name, b, 0, b.length);

        if (resolve) {
            resolveClass(clazz);
        }

        return clazz;
    }
}