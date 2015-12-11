package example.zipentrystream;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipEntryIterator implements Iterator<ZipEntry> {

    private final ZipInputStream in;

    private ZipEntry entry;

    public ZipEntryIterator(ZipInputStream in) {
        this.in = in;
        this.entry = getNextEntry();
    }

    private ZipEntry getNextEntry() {
        try {
            return in.getNextEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return entry != null;
    }

    @Override
    public ZipEntry next() {
        ZipEntry temp = entry;
        if (temp == null) {
            throw new NoSuchElementException();
        }
        entry = getNextEntry();
        return temp;
    }

    public static Stream<ZipEntry> toStream(ZipInputStream in) {
        Iterator<ZipEntry> iterator = new ZipEntryIterator(in);
        Spliterator<ZipEntry> spliterator = Spliterators.spliterator(iterator,
                Long.MAX_VALUE, 0);
        return StreamSupport.stream(spliterator, false);
    }
}
