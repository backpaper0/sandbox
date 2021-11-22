package example.unixdomainsocket;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @see <a href="https://openjdk.java.net/jeps/380">JEP 380: Unix-Domain Socket Channels</a>
 *
 */
public class UnixDomainSocketServerExample {

	public static void main(String[] args) throws IOException {
		Path path = Path.of("foo.socket");
		try (ServerSocketChannel ssc = ServerSocketChannel.open(StandardProtocolFamily.UNIX)) {
			SocketAddress local = UnixDomainSocketAddress.of(path);
			ssc.bind(local);
			try (SocketChannel sc = ssc.accept()) {
				ByteBuffer buf = ByteBuffer.allocate(1024);
				sc.read(buf);
				System.out.println(new String(buf.array(), StandardCharsets.UTF_8));
			}
		} finally {
			Files.deleteIfExists(path);
		}
	}
}
