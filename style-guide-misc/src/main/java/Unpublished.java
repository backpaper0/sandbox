import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Unpublished {

    public static void main(final String[] args) throws Exception {
        final var u = new Unpublished();

        //java.lang
        u.builder(Appendable.class).buildAndAdd();
        u.builder(AutoCloseable.class).buildAndAdd();
        u.builder(CharSequence.class).buildAndAdd();
        u.builder(Comparable.class).buildAndAdd();
        u.builder(Iterable.class).buildAndAdd();
        u.builder(Readable.class).buildAndAdd();
        u.builder(Runnable.class).buildAndAdd();
        u.builder(Boolean.class).buildAndAdd();
        u.builder(Byte.class).buildAndAdd();
        u.builder(Character.class).buildAndAdd();
        u.builder(Character.Subset.class).buildAndAdd();
        u.builder(Character.UnicodeBlock.class).buildAndAdd();
        u.builder(Class.class).buildAndAdd();
        u.builder(Double.class).buildAndAdd();
        u.builder(Enum.class).buildAndAdd();
        u.builder(Float.class).buildAndAdd();
        u.builder(Integer.class).buildAndAdd();
        u.builder(Long.class).buildAndAdd();
        u.builder(Math.class).buildAndAdd();
        u.builder(Number.class).buildAndAdd();
        u.builder(Object.class).buildAndAdd();
        u.builder(Short.class).buildAndAdd();
        u.builder(String.class).buildAndAdd();
        u.builder(StringBuilder.class).buildAndAdd();
        u.builder(ArithmeticException.class).buildAndAdd();
        u.builder(ArrayIndexOutOfBoundsException.class).buildAndAdd();
        u.builder(ClassCastException.class).buildAndAdd();
        u.builder(IllegalArgumentException.class).buildAndAdd();
        u.builder(IllegalStateException.class).buildAndAdd();
        u.builder(NullPointerException.class).buildAndAdd();
        u.builder(NumberFormatException.class).buildAndAdd();
        u.builder(Deprecated.class).buildAndAdd();
        u.builder(FunctionalInterface.class).buildAndAdd();
        u.builder(Override.class).buildAndAdd();
        u.builder(SafeVarargs.class).buildAndAdd();

        final Writer out = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        u.writeTo(out);
        out.flush();
    }

    private final List<ClassAndMethod> classAndMethods = new ArrayList<>();

    void writeTo(final Writer out) throws IOException {
        for (final ClassAndMethod classAndMethod : classAndMethods) {
            classAndMethod.writeTo(out);
        }
    }

    Builder builder(final Class<?> clazz) {
        return new Builder(this, clazz);
    }

    void add(final ClassAndMethod classAndMethod) {
        classAndMethods.add(classAndMethod);
    }

    static class Builder {

        private final Unpublished unpublished;
        private final Class<?> clazz;
        private final List<String> includes = new ArrayList<>();
        private final List<String> excludes = new ArrayList<>();

        public Builder(final Unpublished unpublished, final Class<?> clazz) {
            this.unpublished = unpublished;
            this.clazz = clazz;
        }

        public Builder includes(final String... methods) {
            Arrays.stream(methods).forEach(includes::add);
            return this;
        }

        public Builder excludes(final String... methods) {
            Arrays.stream(methods).forEach(excludes::add);
            return this;
        }

        public void buildAndAdd() {
            if (includes.isEmpty()) {
                if (excludes.isEmpty()) {
                    unpublished.add(new ClassAndMethod(clazz, null));
                } else {
                    //TODO
                    throw new UnsupportedOperationException();
                }
            } else {
                if (excludes.isEmpty()) {
                    //TODO
                    throw new UnsupportedOperationException();
                } else {
                    //TODO
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    static class ClassAndMethod {

        private final Class<?> clazz;
        private final Method method;

        public ClassAndMethod(final Class<?> clazz, final Method method) {
            this.clazz = clazz;
            this.method = method;
        }

        public void writeTo(final Writer out) throws IOException {
            if (method != null) {
                throw new UnsupportedOperationException();
            } else {
                out.write(clazz.getName());
            }
            out.write("\n");
        }
    }
}
