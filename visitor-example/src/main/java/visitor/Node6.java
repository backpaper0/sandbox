package visitor;

import java.io.IOException;
import java.io.Writer;

public interface Node6 {

    <R, P> R accept(Visitor6<R, P> visitor, P parameter);
}

class NumNode6 implements Node6 {

    public final int value;

    public NumNode6(int value) {
        this.value = value;
    }

    @Override public <R, P> R accept(Visitor6<R, P> visitor, P parameter) {
        return visitor.visit(this, parameter);
    }
}

class AddNode6 implements Node6 {

    public final Node6 left;
    public final Node6 right;

    public AddNode6(Node6 left, Node6 right) {
        this.left = left;
        this.right = right;
    }

    @Override public <R, P> R accept(Visitor6<R, P> visitor, P parameter) {
        return visitor.visit(this, parameter);
    }
}

interface Visitor6<R, P> {

    R visit(NumNode6 node, P parameter);

    R visit(AddNode6 node, P parameter);
}

class Calclurator6 implements Visitor6<Integer, Void> {

    @Override public Integer visit(NumNode6 node, Void parameter) {
        return node.value;
    }

    @Override public Integer visit(AddNode6 node, Void parameter) {
        int left = node.left.accept(this, parameter);
        int right = node.right.accept(this, parameter);
        return left + right;
    }
}

class Printer6 implements Visitor6<Void, Writer> {

    @Override public Void visit(NumNode6 node, Writer parameter) {
        try {
            parameter.write(String.valueOf(node.value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override public Void visit(AddNode6 node, Writer parameter) {
        try {
            parameter.write("(");
            node.left.accept(this, parameter);
            parameter.write("+");
            node.right.accept(this, parameter);
            parameter.write(")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
