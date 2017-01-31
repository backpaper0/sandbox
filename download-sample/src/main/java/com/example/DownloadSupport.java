package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public interface DownloadSupport {

    String createFile(FileOutput output);

    void readFile(String key, FileInput input);

    public interface FileOutput {
        void write(OutputStream out) throws IOException;
    }

    public interface FileInput {
        void read(InputStream in) throws IOException;
    }

    public static class DownloadException extends RuntimeException {

        public DownloadException() {
        }

        public DownloadException(Throwable cause) {
            super(cause);
        }
    }

    public static class DownloadTimeoutException extends DownloadException {
    }

    public static class DownloadInvalidException extends DownloadException {
    }

    public static class DownloadIOException extends DownloadException {

        public DownloadIOException(IOException cause) {
            super(cause);
        }
    }

    public static class DownloadGeneralSecurityException extends DownloadException {

        public DownloadGeneralSecurityException(GeneralSecurityException cause) {
            super(cause);
        }
    }

    public static class DefaultDownloadSupport implements DownloadSupport {

        private final Path tempDir;
        private final Key secretKey;
        private final long expireSeconds;

        public DefaultDownloadSupport(String tempDir, String secretKey, long expireSeconds) {
            this.tempDir = Paths.get(Objects.requireNonNull(tempDir));
            this.secretKey = new SecretKeySpec(
                    Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            this.expireSeconds = expireSeconds;
        }

        @Override
        public String createFile(FileOutput output) {
            try {
                Files.createDirectories(tempDir);
                long timeout = System.currentTimeMillis()
                        + TimeUnit.SECONDS.toMillis(expireSeconds);
                Path file = tempDir.resolve(UUID.randomUUID().toString() + '$' + timeout);
                try (OutputStream out = Files.newOutputStream(file)) {
                    output.write(out);
                }
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(secretKey);
                mac.update(file.getFileName().toString().getBytes(StandardCharsets.UTF_8));
                Base64.Encoder encoder = Base64.getUrlEncoder();
                return encoder.encodeToString(
                        file.getFileName().toString().getBytes(StandardCharsets.UTF_8))
                        + '$' + encoder.encodeToString(mac.doFinal());
            } catch (IOException e) {
                throw new DownloadIOException(e);
            } catch (GeneralSecurityException e) {
                throw new DownloadGeneralSecurityException(e);
            }
        }

        @Override
        public void readFile(String key, FileInput input) {
            try {
                Base64.Decoder decoder = Base64.getUrlDecoder();
                int index1 = key.lastIndexOf('$');
                Path file = tempDir.resolve(new String(decoder.decode(key.substring(0, index1)),
                        StandardCharsets.UTF_8));
                int index2 = file.getFileName().toString().indexOf('$');
                long timeout = Long.parseLong(file.getFileName().toString().substring(index2 + 1));
                if (timeout < System.currentTimeMillis()) {
                    throw new DownloadTimeoutException();
                }
                byte[] hash = decoder.decode(key.substring(index1 + 1));
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(secretKey);
                mac.update(file.getFileName().toString().getBytes(StandardCharsets.UTF_8));
                if (Arrays.equals(hash, mac.doFinal()) == false) {
                    throw new DownloadInvalidException();
                }
                try (InputStream in = Files.newInputStream(file)) {
                    input.read(in);
                }
            } catch (IOException e) {
                throw new DownloadIOException(e);
            } catch (GeneralSecurityException e) {
                throw new DownloadGeneralSecurityException(e);
            }
        }
    }
}
