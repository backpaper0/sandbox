package example.javacompiler.classes.impl;

import example.javacompiler.classes.Baz;
import example.javacompiler.classes.MyComponent;
import example.javacompiler.classes.Qux;

@MyComponent
public class BazImpl implements Baz {

    private Qux qux;

    public BazImpl(Qux qux) {
        this.qux = qux;
    }

    public Qux getQux() {
        return qux;
    }
}
