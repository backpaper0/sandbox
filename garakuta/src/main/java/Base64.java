import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public final class Base64 {

	private static final char[] cs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	private Base64() {
		throw new UnsupportedOperationException();
	}

	public static void encode(final InputStream in, final Writer out) throws IOException {
		int read;
		int index;
		int written = 0;
		while (true) {
			read = in.read();
			if (read == -1) {
				break;
			}
			index = read >> 2;
			out.write(cs[index & 0x3f]);
			written++;

			index = read << 4;
			read = in.read();
			if (read == -1) {
				out.write(cs[index & 0x3f]);
				written++;
				break;
			}
			index |= read >> 4;
			out.write(cs[index & 0x3f]);
			written++;

			index = read << 2;
			read = in.read();
			if (read == -1) {
				out.write(cs[index & 0x3f]);
				written++;
				break;
			}
			index |= read >> 6;
			out.write(cs[index & 0x3f]);
			written++;

			index = read;
			out.write(cs[index & 0x3f]);
			written++;
		}
		while (written % 4 != 0) {
			out.write('=');
			written++;
		}
	}

	public static void decode(final Reader in, final OutputStream out) throws IOException {
		int read;
		int index;
		int b;
		while (true) {
			read = in.read();
			if (read == -1 || read == '=') {
				break;
			}
			index = toIndex(read);
			b = index << 2;
			read = in.read();
			if (read == -1 || read == '=') {
				break;
			}
			index = toIndex(read);
			b |= index >> 4;
			out.write(b);

			b = index << 4;
			read = in.read();
			if (read == -1 || read == '=') {
				break;
			}
			index = toIndex(read);
			b |= index >> 2;
			out.write(b);

			b = index << 6;
			read = in.read();
			if (read == -1 || read == '=') {
				break;
			}
			index = toIndex(read);
			b |= index;
			out.write(b);
		}
		while (-1 != in.read())
			;
	}

	private static int toIndex(final int c) {
		if (c == '+') {
			return 62;
		} else if (c == '/') {
			return 63;
		} else if ('A' <= c && c <= 'Z') {
			return c - 65;
		} else if ('a' <= c && c <= 'z') {
			return c - 71;
		} else if ('0' <= c && c <= '9') {
			return c + 4;
		}
		return 0;
	}
}
