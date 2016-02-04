import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PathTest {

    @Test
    public void relativizeで相対パスを得られる() throws Exception {
        Path a = Paths.get("opt", "foo", "bar", "baz");
        Path b = Paths.get("opt", "foo");
        Path c = b.relativize(a);
        assertThat(c, is(Paths.get("bar", "baz")));
    }
}
