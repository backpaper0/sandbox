package visitor;

import java.io.IOException;
import java.io.Writer;

public interface Node7 {

    <R, P, E extends Exception> R accept(Visitor7<R, P, E> visitor, P parameter)
            throws E;
}

class NumNode7 implements Node7 {

    public final int value;

    public NumNode7(int value) {
        this.value = value;
    }

    @Override public <R, P, E extends Exception> R accept(
            Visitor7<R, P, E> visitor, P parameter) throws E {
        return visitor.visit(this, parameter);
    }
}

class AddNode7 implements Node7 {

    public final Node7 left;
    public final Node7 right;

    public AddNode7(Node7 left, Node7 right) {
        this.left = left;
        this.right = right;
    }

    @Override public <R, P, E extends Exception> R accept(
            Visitor7<R, P, E> visitor, P parameter) throws E {
        return visitor.visit(this, parameter);
    }
}

interface Visitor7<R, P, E extends Exception> {

    R visit(NumNode7 node, P parameter) throws E;

    R visit(AddNode7 node, P parameter) throws E;
}

class Calclurator7 implements Visitor7<Integer, Void, RuntimeException> {

    @Override public Integer visit(NumNode7 node, Void parameter) {
        return node.value;
    }

    @Override public Integer visit(AddNode7 node, Void parameter) {
        int left = node.left.accept(this, parameter);
        int right = node.right.accept(this, parameter);
        return left + right;
    }
}

class Printer7 implements Visitor7<Void, Writer, IOException> {

    @Override public Void visit(NumNode7 node, Writer parameter)
            throws IOException {
        parameter.write(String.valueOf(node.value));
        return null;
    }

    @Override public Void visit(AddNode7 node, Writer parameter)
            throws IOException {
        parameter.write("(");
        node.left.accept(this, parameter);
        parameter.write("+");
        node.right.accept(this, parameter);
        parameter.write(")");
        return null;
    }
}
