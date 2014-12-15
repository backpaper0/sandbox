package example;

import java.util.Optional;

import javax.ws.rs.ext.ParamConverter;

public class OptionalParamConverter implements ParamConverter<Optional<?>> {

    @Override
    public Optional<?> fromString(String value) {
        return Optional.ofNullable(value);
    }

    @Override
    public String toString(Optional<?> value) {
        return value.map(Object::toString).orElse(null);
    }
}
