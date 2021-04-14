package example.staticinitializer;

public class Bar {
	static {
		if (true) {
			throw new FooBarException();
		}
	}
}
