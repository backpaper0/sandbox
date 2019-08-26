package security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlocksIterator implements Iterator<byte[]> {

    private boolean processedLastBlock;

    private boolean written0x80;

    private final int blockSize;

    private final InputStream in;

    private final int suffixSize;

    private final Length length;

    public BlocksIterator(final int blockSize, final int suffixSize, final InputStream in) {
        this.blockSize = blockSize;
        this.suffixSize = suffixSize;
        this.in = in;
        this.length = new Length(suffixSize);
    }

    @Override
    public boolean hasNext() {
        return processedLastBlock == false;
    }

    @Override
    public byte[] next() {
        if (hasNext() == false) {
            throw new NoSuchElementException();
        }
        try {
            final byte[] bs = new byte[blockSize];
            int i;
            int index = 0;
            while (-1 != (i = in.read())) {
                bs[index++] = (byte) i;
                length.increment();
                if (index >= blockSize) {
                    break;
                }
            }
            if (written0x80 == false && index < blockSize) {
                bs[index++] = (byte) 0x80;
                written0x80 = true;
            }
            if (index < blockSize - suffixSize) {
                length.writeTo(bs, blockSize - suffixSize);
                processedLastBlock = true;
            }
            return bs;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
