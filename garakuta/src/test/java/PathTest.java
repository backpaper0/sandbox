import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class PathTest {

	@Test
	void relativizeで相対パスを得られる() throws Exception {
		final Path a = Path.of("opt", "foo", "bar", "baz");
		final Path b = Path.of("opt", "foo");
		final Path c = b.relativize(a);
		assertEquals(Path.of("bar", "baz"), c);
	}
}
