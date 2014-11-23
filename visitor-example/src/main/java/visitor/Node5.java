package visitor;

import java.io.IOException;
import java.io.Writer;

public interface Node5 {

    int accept(Visitor5 visitor);
}

class NumNode5 implements Node5 {

    public final int value;

    public NumNode5(int value) {
        this.value = value;
    }

    @Override public int accept(Visitor5 visitor) {
        return visitor.visit(this);
    }
}

class AddNode5 implements Node5 {

    public final Node5 left;
    public final Node5 right;

    public AddNode5(Node5 left, Node5 right) {
        this.left = left;
        this.right = right;
    }

    @Override public int accept(Visitor5 visitor) {
        return visitor.visit(this);
    }
}

interface Visitor5 {

    int visit(NumNode5 node);

    int visit(AddNode5 node);
}

class Calclurator5 implements Visitor5 {

    @Override public int visit(NumNode5 node) {
        return node.value;
    }

    @Override public int visit(AddNode5 node) {
        int left = node.left.accept(this);
        int right = node.right.accept(this);
        return left + right;
    }
}

class Printer5 implements Visitor5 {

    private final Writer out;

    public Printer5(Writer out) {
        this.out = out;
    }

    @Override public int visit(NumNode5 node) {
        try {
            out.write(String.valueOf(node.value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override public int visit(AddNode5 node) {
        try {
            out.write("(");
            node.left.accept(this);
            out.write("+");
            node.right.accept(this);
            out.write(")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
