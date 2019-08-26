package security;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HmacSample {

    public static void main(final String[] args) throws Exception {

        sampleMacHmacSHA256();

        sampleMacPBEWithHmacSHA256();

        sampleKeyPBKDF2WithHmacSHA256();
    }

    /*
     * SHA-256をメッセージダイジェストアルゴリズムとするHmacSHA*アルゴリズム。
     */
    static void sampleMacHmacSHA256() throws Exception {
        final byte[] password = "secret".getBytes();
        final Key key = new SecretKeySpec(password, "HmacSHA256");

        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);

        final byte[] value = mac.doFinal("Hello, world!".getBytes());
        System.out.println(toString(value));
    }

    /*
     * パスワードベースの暗号アルゴリズムによるメッセージ認証コード。
     */
    static void sampleMacPBEWithHmacSHA256() throws Exception {
        final byte[] password = "secret".getBytes();
        final Key key = new SecretKeySpec(password, "PBEWithHmacSHA256");

        //Salt must be at least 8 bytes long
        final byte[] salt = "saltsalt".getBytes();

        //ストレッチング回数
        final int iterationCount = 10000;

        final AlgorithmParameterSpec params = new PBEParameterSpec(salt, iterationCount);

        final Mac mac = Mac.getInstance("PBEWithHmacSHA256");
        mac.init(key, params);

        final byte[] value = mac.doFinal("Hello, world!".getBytes());
        System.out.println(toString(value));
    }

    /*
     * パスワードベースの鍵派生アルゴリズム。
     * 擬似乱数関数にはHmacSHA256を使用。
     */
    static void sampleKeyPBKDF2WithHmacSHA256() throws Exception {
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        final char[] password = "secret".toCharArray();

        final byte[] salt = "salt".getBytes();

        //ストレッチング回数
        final int iterationCount = 10000;

        final int keyLength = 256;

        final KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);

        final SecretKey key = factory.generateSecret(keySpec);
        System.out.println(toString(key.getEncoded()));
    }

    static String toString(final byte[] bs) {
        return IntStream.range(0, bs.length).mapToObj(i -> String.format("%02x", bs[i] & 0xff))
                .collect(Collectors.joining());
    }
}