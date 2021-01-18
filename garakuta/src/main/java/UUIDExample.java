import java.util.UUID;

public class UUIDExample {

	public static void main(final String[] args) {
		final UUID uuid1 = UUID.randomUUID();
		System.out.println(uuid1);

		final byte[] bs = new byte[16];
		for (int i = 0; i < 8; i++) {
			bs[i] = (byte) (uuid1.getMostSignificantBits() >> (64 - 8 * (i + 1)) & 0xff);
		}
		for (int i = 0; i < 8; i++) {
			bs[i + 8] = (byte) (uuid1.getLeastSignificantBits() >> (64 - 8 * (i + 1)) & 0xff);
		}

		final StringBuilder buf = new StringBuilder();
		final int[] is = { 0, 4, 6, 8, 10 };
		for (int i = 0; i < is.length; i++) {
			final boolean notLast = (i + 1) < is.length;
			final int beginIndex = is[i];
			final int endIndex = (notLast ? is[i + 1] : bs.length);
			for (int j = beginIndex; j < endIndex; j++) {
				buf.append(String.format("%02x", bs[j]));
			}
			if (notLast) {
				buf.append('-');
			}
		}

		final UUID uuid2 = UUID.fromString(buf.toString());
		System.out.println(uuid2);
	}
}
