package com.example;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class Tests {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {

		final JavaArchive jar1 = ShrinkWrap.create(JavaArchive.class)
				.addClass(Initializer.class)
				.addClass(Foo.class)
				.addClass(Bar.class)
				.addAsResource("META-INF/services/javax.servlet.ServletContainerInitializer");

		final JavaArchive jar2 = ShrinkWrap.create(JavaArchive.class)
				.addClass(Baz.class);

		final JavaArchive jar3 = ShrinkWrap.create(JavaArchive.class)
				.addClass(BarServlet.class);

		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(FooServlet.class, Qux.class)
				.addAsLibraries(jar1)
				.addAsLibraries(jar2)
				.addAsLibraries(jar3);
	}

	@Test
	public void testServletContainerInitializer(@ArquillianResource URL resource) throws Exception {
		final HttpURLConnection con = (HttpURLConnection) new URL(resource, "/test/foo")
				.openConnection();
		final List<String> lines = new ArrayList<String>();
		final InputStream in = con.getInputStream();
		try {
			final BufferedReader buf = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			String line;
			while (null != (line = buf.readLine())) {
				lines.add(line);
			}
		} finally {
			in.close();
		}
		final List<String> expected = Arrays.asList(
				"com.example.Bar",
				"com.example.Baz",
				"com.example.Qux");
		assertEquals(expected, lines);
	}

	@Test
	public void testServletInJar(@ArquillianResource URL resource) throws Exception {
		final HttpURLConnection con = (HttpURLConnection) new URL(resource, "/test/bar")
				.openConnection();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final InputStream in = con.getInputStream();
		try {
			int i;
			final byte[] b = new byte[Math.max(con.getContentLength(), 1024)];
			while (-1 != (i = in.read(b))) {
				out.write(b, 0, i);
			}
		} finally {
			in.close();
		}
		final String expected = "bar";
		assertEquals(expected, out.toString());
	}
}
