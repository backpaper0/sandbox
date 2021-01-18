import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassScanner {

	public static void main(final String[] args) {
		final ClassScanner cs = new ClassScanner();
		final Set<Class<?>> classes = cs.scanClasses(cs.getClass().getClassLoader());
		classes.forEach(System.out::println);
	}

	public Set<Class<?>> scanClasses(final ClassLoader cl) {
		final Set<Class<?>> classes = new HashSet<>();
		try {
			final Enumeration<URL> resources = cl.getResources("");
			while (resources.hasMoreElements()) {
				final URL resource = resources.nextElement();
				final Path root = Paths.get(resource.toURI());
				final Set<Class<?>> cs = scanClasses(cl, root);
				classes.addAll(cs);
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return classes;
	}

	protected Set<Class<?>> scanClasses(final ClassLoader cl, final Path root) {

		final Predicate<Path> isRegularFile = Files::isRegularFile;
		final Predicate<Path> isClassFile = a -> a.getFileName().toString().endsWith(".class");
		final Predicate<Path> predicate = isRegularFile.and(isClassFile);

		final Function<Path, String> toString = Path::toString;
		final UnaryOperator<String> toClassName = a -> a.replace('/', '.').substring(0,
				a.length() - ".class".length());
		final Function<String, Optional<Class<?>>> loadClass = a -> {
			try {
				return Optional.of(cl.loadClass(a));
			} catch (ClassNotFoundException | NoClassDefFoundError e) {
				return Optional.empty();
			}
		};
		final Function<Optional<Class<?>>, Stream<? extends Class<?>>> unwrap = a -> a
				.map(Stream::of)
				.orElseGet(Stream::empty);
		final Function<Path, Stream<? extends Class<?>>> mapper = toString.andThen(toClassName)
				.andThen(loadClass).andThen(unwrap);

		try {
			return Files.walk(root).filter(predicate).map(root::relativize).flatMap(mapper)
					.collect(Collectors.toSet());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
