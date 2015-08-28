package sample;

import org.seasar.doma.*;

@Domain(valueType = String.class)
public class SampleDomain {
    private final String value;
    public SampleDomain(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
