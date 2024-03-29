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

	@SuppressWarnings("incomplete-switch")
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			// test
			String key = "1234567890";
			ByteArrayInputStream in1 = new ByteArrayInputStream("Hello, world!".getBytes());
			ByteArrayOutputStream out1 = new ByteArrayOutputStream();
			enc(key, in1, out1);
			System.out.printf("ser=%s%n", out1);
			ByteArrayInputStream in2 = new ByteArrayInputStream(out1.toByteArray());
			ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			dec(key, in2, out2);
			System.out.printf("des=%s%n", out2);
			return;
		}
		String type = args[0];
		if (type.contains("help")) {
			System.out.println("encript: SECRET_KEY=secretkey java Enc.java e /path/to/plain");
			System.out.println("decript: SECRET_KEY=secretkey java Enc.java d /path/to/encripted");
			return;
		}
		Path srcfile = Path.of(args[1]);
		String key = System.getenv("SECRET_KEY");
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

	private static Key key(String key) throws Exception {
		byte[] k = MessageDigest.getInstance("md5").digest(key.getBytes());
		return new SecretKeySpec(k, "AES");
	}

	private static void enc(String key, InputStream in, OutputStream out)
			throws Exception {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[c.getBlockSize()];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(iv);
		c.init(Cipher.ENCRYPT_MODE, key(key), new IvParameterSpec(iv));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(iv);
		baos.write(c.doFinal(in.readAllBytes()));
		baos.flush();
		out.write(Base64.getUrlEncoder().encode(baos.toByteArray()));
	}

	private static void dec(String key, InputStream in, OutputStream out)
			throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(
				Base64.getUrlDecoder().decode(in.readAllBytes()));
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[c.getBlockSize()];
		bais.read(iv);
		c.init(Cipher.DECRYPT_MODE, key(key), new IvParameterSpec(iv));
		out.write(c.doFinal(bais.readAllBytes()));
		out.flush();
	}
}
