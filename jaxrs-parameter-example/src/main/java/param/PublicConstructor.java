package param;

/**
 * Stringの引数をひとつだけ受け取るpublicなコンストラクタを持つクラス。
 *
 */
public class PublicConstructor {

    private final String value;

    public PublicConstructor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
