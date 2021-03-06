package javascript;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Base64Test {

	private ScriptEngine engine;

	@Test
	void test() throws Exception {
		final Object o = engine
				.eval("encodeBase64('hello'.split('').map(function(a) { return a.charCodeAt(0); }))");
		assertEquals(Base64.getEncoder().encodeToString("hello".getBytes()), o);
	}

	@BeforeEach
	void setUp() throws Exception {
		final ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByMimeType("application/javascript");
		final String source = new String(Files.readAllBytes(Paths.get(getClass()
				.getResource("/base64.js").toURI())), StandardCharsets.UTF_8);
		engine.eval(source);
	}
}
