package example.resourcebundle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ResourceBundleTest {

	private static Locale defaultLocale;

	@ParameterizedTest(name = "{2}")
	@CsvSource(value = {
			"ja | こんにちは世界！ | クラス(example.resourcebundle.messages_ja)",
			"de | Hallo, welt!     | プロパティファイル(example/resourcebundle/messages_de.properties)",
			"el | Hello, world!    | デフォルトへフォールバック(example/resourcebundle/messages.properties)"
	}, delimiter = '|')
	void test(String language, String expected, String name) {
		final String baseName = "example.resourcebundle.messages";
		final Locale locale = new Locale(language);
		final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
		final String actual = bundle.getString("hello");
		assertEquals(expected, actual);
	}

	@BeforeAll
	static void init() {
		defaultLocale = Locale.getDefault();
		Locale.setDefault(new Locale("en"));
	}

	@AfterAll
	static void restore() {
		Locale.setDefault(defaultLocale);
	}
}
