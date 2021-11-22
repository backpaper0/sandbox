package example.unixdomainsocket;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class UnixDomainSocketClientExample {

	public static void main(String[] args) throws IOException {
		SocketAddress local = UnixDomainSocketAddress.of("foo.socket");
		try (SocketChannel sc = SocketChannel.open(local)) {
			ByteBuffer buf = ByteBuffer.wrap("Hello, world!".getBytes(StandardCharsets.UTF_8));
			sc.write(buf);
		}
	}
}
