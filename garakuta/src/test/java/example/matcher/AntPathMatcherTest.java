package example.matcher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AntPathMatcherTest {

	@ParameterizedTest
	@CsvSource(value = {
			// https://ant.apache.org/manual/dirtasks.html#patterns
			"*.java | x.java      | true",
			"*.java | FooBar.java | true",
			"*.java | FooBar.xml  | false",
			"?.java | x.java   | true",
			"?.java | A.java   | true",
			"?.java | x.xml    | false",
			"?.java | xyz.java | false",
			"/?abc/*/*.java | /xabc/foobar/test.java | true",
			"/test/** | /test/x.java           | true",
			"/test/** | /test/foo/bar/xyz.html | true",
			"/test/** | /xyz.xml               | false",
			"/test/ | /test/x.java           | true", //shorthand
			"/test/ | /test/foo/bar/xyz.html | true", //shorthand
			"/test/ | /xyz.xml               | false", //shorthand
			"**/CVS/* | CVS/Repository                           | true",
			"**/CVS/* | org/apache/CVS/Entries                   | true",
			"**/CVS/* | org/apache/jakarta/tools/ant/CVS/Entries | true",
			"**/CVS/* | org/apache/CVS/foo/bar/Entries           | false",
			"org/apache/jakarta/** | org/apache/jakarta/tools/ant/docs/index.html | true",
			"org/apache/jakarta/** | org/apache/jakarta/test.xml                  | true",
			"org/apache/jakarta/** | org/apache/xyz.java                          | false",
			"org/apache/**/CVS/* | org/apache/CVS/Entries                   | true",
			"org/apache/**/CVS/* | org/apache/jakarta/tools/ant/CVS/Entries | true",
			"org/apache/**/CVS/* | org/apache/CVS/foo/bar/Entries           | false",
	}, delimiter = '|')
	void matches(String pattern, String path, boolean expected) {
		AntPathMatcher sut = new AntPathMatcher(pattern);
		boolean actual = sut.matches(path);
		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvSource(value = {
			"*.java                | [^/]+\\.java",
			"?.java                | [^/]\\.java",
			"/?abc/*/*.java        | /[^/]abc/[^/]+/[^/]+\\.java",
			"/test/**              | /test/.*",
			"/test/                | /test/.*", //shorthand
			"**/CVS/*              | .*CVS/[^/]+",
			"org/apache/jakarta/** | org/apache/jakarta/.*",
			"org/apache/**/CVS/*   | org/apache/.*CVS/[^/]+",
	}, delimiter = '|')
	void parse(String pattern, String expected) {
		AntPathMatcher.PatternParser sut = new AntPathMatcher.PatternParser(pattern);
		String actual = sut.parse();
		assertEquals(expected, actual);
	}
}
