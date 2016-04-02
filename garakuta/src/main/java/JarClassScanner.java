import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runners.JUnit4;

public class JarClassScanner extends ClassScanner {

    public static void main(String[] args) {
        JarClassScanner cs = new JarClassScanner();
        Set<Class<?>> classes = cs.scanClasses(JUnit4.class);
        classes.forEach(System.out::println);
    }

    public Set<Class<?>> scanClasses(Class<?> c) {
        ClassLoader cl = c.getClassLoader();
        Set<Class<?>> classes = new HashSet<>();
        try {
            URI uri = c.getProtectionDomain().getCodeSource().getLocation().toURI();
            URI path = URI.create("jar:" + uri.toString());
            Map<String, ?> env = new HashMap<>();
            try (FileSystem fs = FileSystems.newFileSystem(path, env)) {
                for (Path root : fs.getRootDirectories()) {
                    Set<Class<?>> cs = scanClasses(cl, root);
                    classes.addAll(cs);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }
}
