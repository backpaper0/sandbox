import java.lang.reflect.Constructor;

/**
 * ネタ。
 *
 */
public class VoidInstance {

	public static void main(final String[] args) throws Exception {
		final Constructor<Void> constructor = Void.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		final Void instance = constructor.newInstance();
		System.out.println(instance);
	}
}
