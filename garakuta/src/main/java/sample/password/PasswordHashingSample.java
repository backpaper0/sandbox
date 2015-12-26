package sample.password;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordHashingSample {

    public static void main(String[] args) throws Exception {
        System.out.println("*** hash ***");
        System.out.println(toString(hash("secret")));

        System.out.println("*** hash + salt ***");
        System.out.println(toString(
                hash("secret", "salt1".getBytes(StandardCharsets.UTF_8))));
        System.out.println(toString(
                hash("secret", "salt2".getBytes(StandardCharsets.UTF_8))));

        System.out.println("*** hash + salt + stretching ***");
        System.out.println(toString(hash("secret",
                "salt1".getBytes(StandardCharsets.UTF_8), 10000)));
    }

    private static String toString(byte[] bs) {
        return IntStream.range(0, bs.length)
                .mapToObj(i -> String.format("%02x", bs[i] & 0xff))
                .collect(Collectors.joining());
    }

    static byte[] hash(String password) throws GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bs = password.getBytes(StandardCharsets.UTF_8);
        return md.digest(bs);
    }

    static byte[] hash(String password, byte[] salt)
            throws GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bs = password.getBytes(StandardCharsets.UTF_8);
        bs = Arrays.copyOf(bs, bs.length + salt.length);
        System.arraycopy(salt, 0, bs, bs.length - salt.length, salt.length);
        return md.digest(bs);
    }

    static byte[] hash(String password, byte[] salt, int stretchingSize)
            throws GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bs = password.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < stretchingSize; i++) {
            bs = Arrays.copyOf(bs, bs.length + salt.length);
            System.arraycopy(salt, 0, bs, bs.length - salt.length, salt.length);
            bs = md.digest(bs);
        }
        return bs;
    }
}
