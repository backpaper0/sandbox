package example.zipentrystream;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipEntrySpliterator extends AbstractSpliterator<ZipEntry> {

	private final ZipInputStream in;

	public ZipEntrySpliterator(final ZipInputStream in) {
		super(Long.MAX_VALUE, 0);
		this.in = in;
	}

	@Override
	public boolean tryAdvance(final Consumer<? super ZipEntry> action) {
		try {
			final ZipEntry entry = in.getNextEntry();
			if (entry == null) {
				return false;
			}
			action.accept(entry);
			in.closeEntry();
			return true;
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static Stream<ZipEntry> toStream(final ZipInputStream in) {
		final Spliterator<ZipEntry> spliterator = new ZipEntrySpliterator(in);
		return StreamSupport.stream(spliterator, false);
	}
}
