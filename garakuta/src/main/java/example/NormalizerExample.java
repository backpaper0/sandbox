package example;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class NormalizerExample {

	public static void main(final String[] args) {
		final String s = Normalizer.normalize("ｱｶﾞｻﾞ ﾀﾞﾅ", Form.NFKD);
		System.out.println(s);
	}
}
