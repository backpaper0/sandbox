package security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PublicKeyAndPrivateKeyLoadPKCS8 {

	public static void main(final String[] args) throws Exception {
		publicKey(args[0]);
		privateKey(args[1]);
	}

	static void publicKey(String pathAsString) throws Exception {
		final Path path = Path.of(pathAsString);
		final String encoded = Files
				.readAllLines(path)
				.stream()
				.dropWhile("-----BEGIN PUBLIC KEY-----"::equals)
				.takeWhile(Predicate.not("-----END PUBLIC KEY-----"::equals))
				.collect(Collectors.joining());
		final byte[] decoded = Base64.getMimeDecoder().decode(encoded);
		final KeyFactory factory = KeyFactory.getInstance("RSA");
		final KeySpec keySpec = new X509EncodedKeySpec(decoded);
		final PublicKey publicKey = factory.generatePublic(keySpec);
		System.out.println(publicKey);
	}

	static void privateKey(String pathAsString) throws Exception {
		final Path path = Path.of(pathAsString);
		final String encoded = Files
				.readAllLines(path)
				.stream()
				.dropWhile("-----BEGIN PRIVATE KEY-----"::equals)
				.takeWhile(Predicate.not("-----END PRIVATE KEY-----"::equals))
				.collect(Collectors.joining());
		final byte[] decoded = Base64.getMimeDecoder().decode(encoded);
		final KeyFactory factory = KeyFactory.getInstance("RSA");
		final KeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		final PrivateKey privateKey = factory.generatePrivate(keySpec);
		System.out.println(privateKey);
	}
}
