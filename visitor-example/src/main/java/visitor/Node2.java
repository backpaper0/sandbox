package visitor;

import java.io.IOException;
import java.io.Writer;

public interface Node2 {

    int calc();

    void print(Writer out) throws IOException;
}

class NumNode2 implements Node2 {

    public final int value;

    public NumNode2(int value) {
        this.value = value;
    }

    @Override public int calc() {
        return value;
    }

    @Override public void print(Writer out) throws IOException {
        out.write(String.valueOf(value));
    }
}

class AddNode2 implements Node2 {

    public final Node2 left;
    public final Node2 right;

    public AddNode2(Node2 left, Node2 right) {
        this.left = left;
        this.right = right;
    }

    @Override public int calc() {
        return left.calc() + right.calc();
    }

    @Override public void print(Writer out) throws IOException {
        out.write("(");
        left.print(out);
        out.write("+");
        right.print(out);
        out.write(")");
    }
}
