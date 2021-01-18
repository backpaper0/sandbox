import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Enc {

	public static void main(final String[] args) throws Exception {
		if (args.length == 0) {
			// test
			final String key = "1234567890";
			final ByteArrayInputStream in1 = new ByteArrayInputStream("Hello, world!".getBytes());
			final ByteArrayOutputStream out1 = new ByteArrayOutputStream();
			enc(key, in1, out1);
			System.out.printf("ser=%s%n", out1);
			final ByteArrayInputStream in2 = new ByteArrayInputStream(out1.toByteArray());
			final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			dec(key, in2, out2);
			System.out.printf("des=%s%n", out2);
			return;
		}
		final String type = args[0];
		if (type.contains("help")) {
			System.out.println("encript: SECRET_KEY=secretkey java Enc.java e /path/to/plain");
			System.out.println("decript: SECRET_KEY=secretkey java Enc.java d /path/to/encripted");
			return;
		}
		final Path srcfile = Path.of(args[1]);
		final String key = System.getenv("SECRET_KEY");
		switch (type) {
		case "e":
			try (InputStream in = Files.newInputStream(srcfile)) {
				enc(key, in, System.out);
			}
			break;
		case "d":
			try (InputStream in = Files.newInputStream(srcfile)) {
				dec(key, in, System.out);
			}
			break;
		}
	}

	private static Key key(final String key) throws Exception {
		final byte[] k = MessageDigest.getInstance("md5").digest(key.getBytes());
		return new SecretKeySpec(k, "AES");
	}

	private static void enc(final String key, final InputStream in, final OutputStream out)
			throws Exception {
		final Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final byte[] iv = new byte[c.getBlockSize()];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(iv);
		c.init(Cipher.ENCRYPT_MODE, key(key), new IvParameterSpec(iv));
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(iv);
		baos.write(c.doFinal(in.readAllBytes()));
		baos.flush();
		out.write(Base64.getUrlEncoder().encode(baos.toByteArray()));
	}

	private static void dec(final String key, final InputStream in, final OutputStream out)
			throws Exception {
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				Base64.getUrlDecoder().decode(in.readAllBytes()));
		final Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		final byte[] iv = new byte[c.getBlockSize()];
		bais.read(iv);
		c.init(Cipher.DECRYPT_MODE, key(key), new IvParameterSpec(iv));
		out.write(c.doFinal(bais.readAllBytes()));
		out.flush();
	}
}
