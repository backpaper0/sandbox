package example.javacompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class InMemoryClassFile extends SimpleJavaFileObject {

	private final ByteArrayOutputStream out;

	public InMemoryClassFile(final String name, final Kind kind,
			final ByteArrayOutputStream out) {
		super(URI.create("class:///" + name.replace('.', '/') + kind.extension), kind);
		this.out = out;
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return out;
	}
}
