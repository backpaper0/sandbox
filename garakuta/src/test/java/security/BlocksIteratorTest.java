package security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

public class BlocksIteratorTest {

    @Test
    public void test_512bit_one_block() throws Exception {
        final BlocksIterator it = new BlocksIterator(64, 8,
                new ByteArrayInputStream("abc".getBytes()));
        assertTrue(it.hasNext());
        final byte[] bs = new byte[64];
        bs[0] = 'a';
        bs[1] = 'b';
        bs[2] = 'c';
        bs[3] = (byte) 0x80;
        bs[63] = 24;
        assertArrayEquals(bs, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testName() throws Exception {
        final BlocksIterator it = new BlocksIterator(128, 16,
                new ByteArrayInputStream("abc".getBytes()));
        assertTrue(it.hasNext());
        final byte[] bs = new byte[128];
        bs[0] = 'a';
        bs[1] = 'b';
        bs[2] = 'c';
        bs[3] = (byte) 0x80;
        bs[127] = 24;
        assertArrayEquals(bs, it.next());
        assertFalse(it.hasNext());
    }
}
