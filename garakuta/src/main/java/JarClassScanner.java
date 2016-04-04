import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JarClassScanner extends ClassScanner {

    public static void main(String[] args) {
        JarClassScanner cs = new JarClassScanner();
        //        Set<Class<?>> classes = cs.scanClasses(JUnit4.class);
        //        classes.forEach(System.out::println);

        ClassLoader cl = cs.getClass().getClassLoader();
        Set<Class<?>> classes = Arrays
                .stream(System.getProperty("java.class.path").split(File.pathSeparator))
                .filter(a -> Files.isRegularFile(Paths.get("/" + a)))
                .flatMap(jar -> cs.scanClasses(cl, "jar:file:" + jar).stream())
                .collect(Collectors.toSet());
        classes.forEach(System.out::println);
    }

    public Set<Class<?>> scanClasses(Class<?> c) {
        ClassLoader cl = c.getClassLoader();
        try {
            URI uri = c.getProtectionDomain().getCodeSource().getLocation().toURI();
            return scanClasses(cl, "jar:" + uri.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Class<?>> scanClasses(ClassLoader cl, String jar) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            URI path = URI.create(jar);
            Map<String, ?> env = new HashMap<>();
            try (FileSystem fs = FileSystems.newFileSystem(path, env)) {
                for (Path root : fs.getRootDirectories()) {
                    Set<Class<?>> cs = scanClasses(cl, root);
                    classes.addAll(cs);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }
}
