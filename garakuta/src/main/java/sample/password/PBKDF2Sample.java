package sample.password;

import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Sample {

    public static void main(String[] args) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256");
        char[] password = "secret".toCharArray();
        byte[] salt = "salt".getBytes();
        int iterationCount = 10000;
        int keyLength = 256;
        KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount,
                keyLength);
        SecretKey key = factory.generateSecret(keySpec);
        byte[] hash = key.getEncoded();
        System.out.println(toString(hash));
    }

    private static String toString(byte[] bs) {
        return IntStream.range(0, bs.length)
                .mapToObj(i -> String.format("%02x", bs[i] & 0xff))
                .collect(Collectors.joining());
    }
}
