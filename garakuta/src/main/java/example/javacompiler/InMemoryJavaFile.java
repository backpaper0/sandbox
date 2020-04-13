package example.javacompiler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class InMemoryJavaFile extends SimpleJavaFileObject {

    private final StringWriter out = new StringWriter();

    public InMemoryJavaFile(final String name) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
                Kind.SOURCE);
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
        return out.toString();
    }

    @Override
    public Writer openWriter() throws IOException {
        return out;
    }
}
