package visitor;

public interface Node1 {

    int calc();
}

class NumNode1 implements Node1 {

    public final int value;

    public NumNode1(int value) {
        this.value = value;
    }

    @Override public int calc() {
        return value;
    }
}

class AddNode1 implements Node1 {

    public final Node1 left;
    public final Node1 right;

    public AddNode1(Node1 left, Node1 right) {
        this.left = left;
        this.right = right;
    }

    @Override public int calc() {
        return left.calc() + right.calc();
    }
}
