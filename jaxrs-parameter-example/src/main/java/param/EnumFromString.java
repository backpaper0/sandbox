package param;

/**
 * fromStringが定義されている列挙型。 列挙型の場合は(暗黙的に定義されている)valueOfよりもfromStringが優先される。
 *
 */
public enum EnumFromString {

	SINGLETON;

	private String value;

	public static EnumFromString fromString(String value) {
		SINGLETON.value = value;
		return SINGLETON;
	}

	public String getValue() {
		return value;
	}
}
