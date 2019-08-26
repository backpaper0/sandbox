package example.zipentrystream;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipEntryStreamExample2 {

    public static void main(final String[] args) throws IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream out = new ZipOutputStream(baos)) {

            out.putNextEntry(new ZipEntry("file1"));
            out.write("foo\nbar\nbaz".getBytes());

            out.putNextEntry(new ZipEntry("dir/file2"));
            out.write("foo\nbar\nbaz".getBytes());

            out.putNextEntry(new ZipEntry("dir/file3"));
            out.write("foo\nbar\nbaz".getBytes());
        }

        try (ZipInputStream in = new LoggingZipInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {

            toStream(in).map(InputStreamReader::new).map(BufferedReader::new)
                    .flatMap(BufferedReader::lines)
                    .forEach(System.out::println);
        }
    }

    static Stream<InputStream> toStream(final ZipInputStream in) {
        final Spliterator<InputStream> spliterator = new AbstractSpliterator<>(
                Long.MAX_VALUE, 0) {

            @Override
            public boolean tryAdvance(final Consumer<? super InputStream> action) {
                try {
                    final ZipEntry entry = in.getNextEntry();
                    if (entry == null) {
                        return false;
                    }
                    action.accept(in);
                    in.closeEntry();
                    return true;
                } catch (final IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    static class LoggingZipInputStream extends ZipInputStream {

        public LoggingZipInputStream(final InputStream in) {
            super(in);
            System.out.println("***construct***");
        }

        @Override
        public void close() throws IOException {
            System.out.println("***close***");
        }
    }
}
