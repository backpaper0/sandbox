package param;

import jakarta.ws.rs.ext.ParamConverter;

public class ParamConverterImpl implements ParamConverter<ParamConverted> {

	@Override
	public ParamConverted fromString(String value) {
		return new ParamConvertedImpl(value);
	}

	@Override
	public String toString(ParamConverted value) {
		return value.getValue();
	}
}
