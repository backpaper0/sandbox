package example;

import java.io.Serializable;

public class DeserializeAndConstructor implements Serializable {

	private transient final boolean called;
	private transient final Object foo;
	private transient final Object bar = new Object();

	public DeserializeAndConstructor() {
		this.called = true;
		this.foo = new Object();
	}

	public boolean isCalled() {
		return called;
	}

	public Object getFoo() {
		return foo;
	}

	public Object getBar() {
		return bar;
	}
}
