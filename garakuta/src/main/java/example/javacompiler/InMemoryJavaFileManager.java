package example.javacompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final InMemoryClassLoader classLoader;

    public InMemoryJavaFileManager(final JavaFileManager fileManager,
            final InMemoryClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader(final Location location) {
        return classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(final Location location, final String className,
            final Kind kind,
            final FileObject sibling) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        classLoader.addClass(className, out);
        return new InMemoryClassFile(className, kind, out);
    }
}
