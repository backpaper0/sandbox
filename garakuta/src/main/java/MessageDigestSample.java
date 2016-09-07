import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageDigestSample {
    public static void main(String[] args) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");
        md.update("hello".getBytes());
        md.update("world".getBytes());
        byte[] bs = md.digest();
        String s = IntStream.range(0, bs.length)
                .mapToObj(i -> String.format("%02x", bs[i] & 0xff))
                .collect(Collectors.joining());
        System.out.println(s);
    }
}
