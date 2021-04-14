package example.staticinitializer;

public class StaticInitializerDemo {

	private static Foo foo;

	public static void main(String[] args) throws Throwable {
		System.out.println("start");
		try {
			foo = new Foo();
		} catch (ExceptionInInitializerError e) {
			if (e.getCause() instanceof FooBarException) {
				System.out.println("catch foo");
			} else {
				throw e.getCause();
			}
		}
		try {
			Class.forName("example.staticinitializer.Bar");
		} catch (ExceptionInInitializerError e) {
			if (e.getCause() instanceof FooBarException) {
				System.out.println("catch bar");
			} else {
				throw e.getCause();
			}
		}
		System.out.println("end");
	}
}
