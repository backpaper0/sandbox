package example.javacompiler.classes;

@MyComponent
public class Foo {

	private Bar bar;
	private Baz baz;

	public Foo(Bar bar, Baz baz) {
		this.bar = bar;
		this.baz = baz;
	}

	public Bar getBar() {
		return bar;
	}

	public Baz getBaz() {
		return baz;
	}
}
