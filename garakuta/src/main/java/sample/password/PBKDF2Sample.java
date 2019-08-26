package sample.password;

import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Sample {

    public static void main(final String[] args) throws GeneralSecurityException {
        final SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256");
        final char[] password = "secret".toCharArray();
        final byte[] salt = "salt".getBytes();
        final int iterationCount = 10000;
        final int keyLength = 256;
        final KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount,
                keyLength);
        final SecretKey key = factory.generateSecret(keySpec);
        final byte[] hash = key.getEncoded();
        System.out.println(toString(hash));
    }

    private static String toString(final byte[] bs) {
        return IntStream.range(0, bs.length)
                .mapToObj(i -> String.format("%02x", bs[i] & 0xff))
                .collect(Collectors.joining());
    }
}
