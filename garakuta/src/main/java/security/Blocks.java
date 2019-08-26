package security;

import java.io.InputStream;
import java.util.Iterator;

public class Blocks implements Iterable<byte[]> {

    private final InputStream in;

    private final int blockSize;

    private final int suffixSize;

    public Blocks(final int blockSize, final int suffixSize, final InputStream in) {
        this.blockSize = blockSize;
        this.suffixSize = suffixSize;
        this.in = in;
    }

    @Override
    public Iterator<byte[]> iterator() {
        return new BlocksIterator(blockSize, suffixSize, in);
    }

}
