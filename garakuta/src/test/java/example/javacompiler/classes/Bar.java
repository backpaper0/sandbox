package example.javacompiler.classes;

@MyComponent
public class Bar {

	private Qux qux;

	public Bar(Qux qux) {
		this.qux = qux;
	}

	public Qux getQux() {
		return qux;
	}
}
