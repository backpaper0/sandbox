package param;

/**
 * Stringの引数をひとつだけ受け取る"fromString"という名前のstaticファクトリメソッドを持つクラス。
 *
 */
public abstract class FromString {

	private final String value;

	private FromString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static FromString fromString(String value) {
		return new FromString(value) {
		};
	}
}
