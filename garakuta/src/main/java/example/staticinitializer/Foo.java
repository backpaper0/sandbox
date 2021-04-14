package example.staticinitializer;

public class Foo {
	static {
		if (true) {
			throw new FooBarException();
		}
	}
}
