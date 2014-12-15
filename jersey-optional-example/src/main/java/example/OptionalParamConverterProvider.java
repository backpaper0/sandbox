package example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class OptionalParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
            Type genericType, Annotation[] annotations) {
        if (rawType == Optional.class) {
            return (ParamConverter<T>) new OptionalParamConverter();
        }
        return null;
    }
}
