package sample;

import java.util.Optional;

import org.seasar.doma.Domain;

//自動生成対象となるインターフェース
@Domain(valueType = String.class, factoryMethod = "valueOf")
public interface Hoge {

    String getValue();

    static Hoge valueOf(String value) {
        return Optional.ofNullable(value).map(HogeImpl::new).orElse(null);
    }
}
