public class PackedDecimal {

	public static void main(final String[] args) {
		// https://ja.wikipedia.org/wiki/%E3%83%91%E3%83%83%E3%82%AF10%E9%80%B2%E6%95%B0
		System.out.println(decode(new byte[] { 0b0000_0001, 0b0010_0011, 0b0100_1100 }));
		System.out.println(decode(new byte[] { 0b0000_0001, 0b0010_0011, 0b0100_1101 }));
	}

	static int decode(final byte[] data) {
		int value = 0;
		for (int i = 0; i < data.length; i++) {
			final byte b = data[i];
			value *= 10;
			value += b >> 4 & 0xf;
			if ((i + 1) == data.length) {
				switch (b & 0xf) {
				case 12:
					break;
				case 13:
					value *= -1;
					break;
				default:
					throw new IllegalArgumentException();
				}
			} else {
				value *= 10;
				value += b & 0xf;
			}
		}
		return value;
	}
}