import java.util.function.Consumer;

import org.junit.Test;

public class NullPo {

    @Test(expected = NullPointerException.class)
    public void testFor() throws Exception {
        int[] xs = null;
        for (int x : xs) {
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSwitch() throws Exception {
        String x = null;
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
        Integer x = null;
        int y = x;
    }

    @Test(expected = NullPointerException.class)
    public void testThrow() throws Exception {
        UnsupportedOperationException e = null;
        throw e;
    }

    @Test
    public void testStaticMethod() throws Exception {
        Hoge x = null;
        x.foobar();
    }

    @Test(expected = NullPointerException.class)
    public void testInstantiation() throws Exception {
        new Hoge(a -> a.x.length());
    }

    static class Hoge {

        final String x;

        public Hoge(Consumer<Hoge> c) {
            c.accept(this);
            this.x = "hoge";
        }

        static void foobar() {
        }
    }
}
