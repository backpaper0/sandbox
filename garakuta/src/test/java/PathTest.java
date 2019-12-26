import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    public void relativizeで相対パスを得られる() throws Exception {
        final Path a = Paths.get("opt", "foo", "bar", "baz");
        final Path b = Paths.get("opt", "foo");
        final Path c = b.relativize(a);
        assertEquals(Paths.get("bar", "baz"), c);
    }
}
