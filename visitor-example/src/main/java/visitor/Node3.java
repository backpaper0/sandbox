package visitor;

public interface Node3 {}

class NumNode3 implements Node3 {

    public final int value;

    public NumNode3(int value) {
        this.value = value;
    }
}

class AddNode3 implements Node3 {

    public final Node3 left;
    public final Node3 right;

    public AddNode3(Node3 left, Node3 right) {
        this.left = left;
        this.right = right;
    }
}

class Calclurator3 {

    public int calc(Node3 node) {
        if (node instanceof AddNode3) {
            AddNode3 addNode3 = (AddNode3) node;
            return calc(addNode3.left) + calc(addNode3.right);
        } else if (node instanceof NumNode3) {
            NumNode3 numNode3 = (NumNode3) node;
            return numNode3.value;
        }
        throw new IllegalArgumentException(String.valueOf(node));
    }
}
