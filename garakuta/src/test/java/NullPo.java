import java.util.function.Consumer;

import org.junit.Test;

public class NullPo {

    @Test(expected = NullPointerException.class)
    public void testFor() throws Exception {
        final int[] xs = null;
        for (final int x : xs) {
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSwitch() throws Exception {
        final String x = null;
        switch (x) {
        }
    }

    @Test
    public void testTryWithResource() throws Exception {
        try (AutoCloseable x = null) {
        }
    }

    @Test(expected = NullPointerException.class)
    public void testUnboxing() throws Exception {
        final Integer x = null;
        final int y = x;
    }

    @Test(expected = NullPointerException.class)
    public void testThrow() throws Exception {
        final UnsupportedOperationException e = null;
        throw e;
    }

    @Test
    public void testStaticMethod() throws Exception {
        final Hoge x = null;
        x.foobar();
    }

    @Test(expected = NullPointerException.class)
    public void testInstantiation() throws Exception {
        new Hoge(a -> a.x.length());
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
