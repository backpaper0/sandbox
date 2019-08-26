import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.LongSupplier;

public class StringLiteral {

    public static class A {

        public static String literal = "Hello";
    }

    public static class B {

        public static String literal = "Hello";
    }

    public static void main(final String[] args) throws Exception {

        MyClassLoader loader = new MyClassLoader();
        Class<?> clazz = Class.forName("StringLiteral$B", false, loader);
        Field field = clazz.getDeclaredField("literal");

        System.out.println(A.literal == field.get(null));

        loader = null;
        clazz = null;
        field = null;

        //空きメモリが増えなくなるなるまでGCしまくる
        final LongSupplier f = () -> Runtime.getRuntime().freeMemory();
        long freeMemory = f.getAsLong();
        do {
            System.gc();
            System.out.println("GCしたよ");
        } while (freeMemory < (freeMemory = f.getAsLong()));

        System.out.println(A.literal);
    }

    public static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            if (name.equals("StringLiteral$B") == false) {
                return super.loadClass(name, resolve);
            }
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                final URL url = getResource(name.replace('.', '/') + ".class");
                final URLConnection conn = url.openConnection();
                try (InputStream in = url.openStream()) {
                    final byte[] b = new byte[conn.getContentLength()];
                    int i;
                    while (-1 != (i = in.read(b))) {
                        out.write(b, 0, i);
                    }
                }
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
            final byte[] b = out.toByteArray();
            final Class<?> c = defineClass(name, b, 0, b.length);
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
