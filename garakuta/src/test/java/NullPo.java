import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

public class NullPo {

    @Test
    public void testFor() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            final int[] xs = null;
            for (final int x : xs) {
            }
        });
    }

    @Test
    public void testSwitch() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            final String x = null;
            switch (x) {
            }
        });
    }

    @Test
    public void testTryWithResource() throws Exception {
        try (AutoCloseable x = null) {
        }
    }

    @Test
    public void testUnboxing() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            final Integer x = null;
            final int y = x;
        });
    }

    @Test
    public void testThrow() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            final UnsupportedOperationException e = null;
            throw e;
        });
    }

    @Test
    public void testStaticMethod() throws Exception {
        final Hoge x = null;
        x.foobar();
    }

    @Test
    public void testInstantiation() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            new Hoge(a -> a.x.length());
        });
    }

    static class Hoge {

        final String x;

        public Hoge(final Consumer<Hoge> c) {
            c.accept(this);
            this.x = "hoge";
        }

        static void foobar() {
        }
    }
}
