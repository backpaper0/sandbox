package security;

import java.io.InputStream;
import java.util.Iterator;

public class Blocks implements Iterable<byte[]> {

    private final InputStream in;

    private int blockSize;

    private int suffixSize;

    public Blocks(int blockSize, int suffixSize, InputStream in) {
        this.blockSize = blockSize;
        this.suffixSize = suffixSize;
        this.in = in;
    }

    @Override
    public Iterator<byte[]> iterator() {
        return new BlocksIterator(blockSize, suffixSize, in);
    }

}
