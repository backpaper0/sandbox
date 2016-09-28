package example.zipentrystream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipEntryStreamExample1 {

    public static void main(String[] args) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream out = new ZipOutputStream(baos)) {

            out.putNextEntry(new ZipEntry("file1"));
            out.write("foo".getBytes());

            out.putNextEntry(new ZipEntry("dir/file2"));
            out.write("bar".getBytes());

            out.putNextEntry(new ZipEntry("dir/file3"));
            out.write("baz".getBytes());
        }

        try (ZipInputStream in = new ZipInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {

            ZipEntrySpliterator.toStream(in).map(a -> toString(in, a))
                    .forEach(System.out::println);
        }

        try (ZipInputStream in = new ZipInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {

            ZipEntryIterator.toStream(in).map(a -> toString(in, a))
                    .forEach(System.out::println);
        }
    }

    static String toString(ZipInputStream in, ZipEntry entry) {
        try {
            byte[] b = new byte[1024];
            int i;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (-1 != (i = in.read(b))) {
                out.write(b, 0, i);
            }
            return entry.getName() + ": " + out.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
