package sample2;

import java.util.Optional;

import org.seasar.doma.Domain;

//自動生成対象となる抽象クラス
@Domain(valueType = String.class, factoryMethod = "valueOf")
public abstract class Fuga {
    private final String value;

    protected Fuga(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Fuga valueOf(String value) {
        return Optional.ofNullable(value).map(FugaImpl::new).orElse(null);
    }
}
