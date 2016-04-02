import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.runners.JUnit4;

public class JarClassScanner {

    public static void main(String[] args) {
        JarClassScanner cs = new JarClassScanner();
        Set<Class<?>> classes = cs.scanClasses(JUnit4.class);
        classes.forEach(System.out::println);
    }

    public Set<Class<?>> scanClasses(Class<?> c) {

        Set<Class<?>> classes = new HashSet<>();

        Predicate<Path> isRegularFile = Files::isRegularFile;
        Predicate<Path> isClassFile = a -> a.getFileName().toString().endsWith(".class");
        Predicate<Path> predicate = isRegularFile.and(isClassFile);

        Function<Path, String> toString = Path::toString;
        UnaryOperator<String> toClassName = a -> a.replace('/', '.').substring(1,
                a.length() - ".class".length());
        Function<String, Class<?>> loadClass = a -> {
            try {
                return Class.forName(a);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        Function<Path, Class<?>> mapper = toString.andThen(toClassName).andThen(loadClass);

        try {
            URI uri = c.getProtectionDomain().getCodeSource().getLocation().toURI();
            URI path = URI.create("jar:" + uri.toString());
            Map<String, ?> env = new HashMap<>();
            try (FileSystem fs = FileSystems.newFileSystem(path, env)) {
                for (Path root : fs.getRootDirectories()) {
                    Files.walk(root).filter(predicate).map(mapper).forEach(System.out::println);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }
}
