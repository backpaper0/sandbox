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

    public static void main(String[] args) throws Exception {

        MyClassLoader loader = new MyClassLoader();
        Class<?> clazz = Class.forName("StringLiteral$B", false, loader);
        Field field = clazz.getDeclaredField("literal");

        System.out.println(A.literal == field.get(null));

        loader = null;
        clazz = null;
        field = null;

        //空きメモリが増えなくなるなるまでGCしまくる
        LongSupplier f = () -> Runtime.getRuntime().freeMemory();
        long freeMemory = f.getAsLong();
        do {
            System.gc();
            System.out.println("GCしたよ");
        } while (freeMemory < (freeMemory = f.getAsLong()));

        System.out.println(A.literal);
    }

    public static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            if (name.equals("StringLiteral$B") == false) {
                return super.loadClass(name, resolve);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                URL url = getResource(name.replace('.', '/') + ".class");
                URLConnection conn = url.openConnection();
                try (InputStream in = url.openStream()) {
                    byte[] b = new byte[conn.getContentLength()];
                    int i;
                    while (-1 != (i = in.read(b))) {
                        out.write(b, 0, i);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            byte[] b = out.toByteArray();
            Class<?> c = defineClass(name, b, 0, b.length);
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
