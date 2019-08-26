package security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class BlocksIteratorTest {

    @Test
    public void test_512bit_one_block() throws Exception {
        final BlocksIterator it = new BlocksIterator(64, 8, new ByteArrayInputStream("abc".getBytes()));
        assertThat(it.hasNext(), is(true));
        final byte[] bs = new byte[64];
        bs[0] = 'a';
        bs[1] = 'b';
        bs[2] = 'c';
        bs[3] = (byte) 0x80;
        bs[63] = 24;
        assertThat(it.next(), is(bs));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testName() throws Exception {
        final BlocksIterator it = new BlocksIterator(128, 16, new ByteArrayInputStream("abc".getBytes()));
        assertThat(it.hasNext(), is(true));
        final byte[] bs = new byte[128];
        bs[0] = 'a';
        bs[1] = 'b';
        bs[2] = 'c';
        bs[3] = (byte) 0x80;
        bs[127] = 24;
        assertThat(it.next(), is(bs));
        assertThat(it.hasNext(), is(false));
    }
}
