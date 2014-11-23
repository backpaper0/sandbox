package param;

/**
 * Stringの引数をひとつだけ受け取る"valueOf"という名前のstaticファクトリメソッドを持つクラス。
 *
 */
public abstract class ValueOf {

    private final String value;

    private ValueOf(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ValueOf valueOf(String value) {
        return new ValueOf(value) {
        };
    }
}
