package sample;

import java.util.Objects;

//アクセサを実装するスケルトン
public abstract class HogeSkeleton implements Hoge {

    protected final String value;

    public HogeSkeleton(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String getValue() {
        return value;
    }
}
