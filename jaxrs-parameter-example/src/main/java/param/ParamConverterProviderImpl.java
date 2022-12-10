package param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;

public class ParamConverterProviderImpl implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType,
			Type genericType, Annotation[] annotations) {
		if (ParamConverted.class.isAssignableFrom(rawType)) {
			return (ParamConverter<T>) new ParamConverterImpl();
		}
		return null;
	}
}
