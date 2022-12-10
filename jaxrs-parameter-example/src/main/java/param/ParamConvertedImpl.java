package param;

public class ParamConvertedImpl implements ParamConverted {

	private final String value;

	public ParamConvertedImpl(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}
