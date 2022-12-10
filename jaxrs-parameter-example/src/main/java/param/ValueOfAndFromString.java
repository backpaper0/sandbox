package param;

/**
 * valueOfとfromStringのどちらも定義されているクラス。 この場合はvalueOfが優先される。
 *
 */
public abstract class ValueOfAndFromString {

	private final String value;

	private ValueOfAndFromString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ValueOfAndFromString valueOf(String value) {
		return new ValueOfAndFromString(value) {
		};
	}

	public static ValueOfAndFromString fromString(String value) {
		throw new RuntimeException();
	}
}
