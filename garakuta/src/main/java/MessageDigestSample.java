import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageDigestSample {
	public static void main(final String[] args) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("md5");
		md.update("hello".getBytes());
		md.update("world".getBytes());
		final byte[] bs = md.digest();
		final String s = IntStream.range(0, bs.length)
				.mapToObj(i -> String.format("%02x", bs[i] & 0xff))
				.collect(Collectors.joining());
		System.out.println(s);
	}
}
