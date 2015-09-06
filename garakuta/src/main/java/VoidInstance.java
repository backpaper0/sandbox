import java.lang.reflect.Constructor;

/**
 * ネタ。
 *
 */
public class VoidInstance {

    public static void main(String[] args) throws Exception {
        Constructor<Void> constructor = Void.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Void instance = constructor.newInstance();
        System.out.println(instance);
    }
}
