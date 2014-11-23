package visitor;

public interface Node4 {}

class NumNode4 implements Node4 {

    public final int value;

    public NumNode4(int value) {
        this.value = value;
    }
}

class AddNode4 implements Node4 {

    public final Node4 left;
    public final Node4 right;

    public AddNode4(Node4 left, Node4 right) {
        this.left = left;
        this.right = right;
    }
}

interface Visitor4 {

    int visit(NumNode4 node);

    int visit(AddNode4 node);
}

class Calclurator4 implements Visitor4 {

    public int calc(Node4 node) {
        if (node instanceof NumNode4) {
            return visit((NumNode4) node);
        } else if (node instanceof AddNode4) {
            return visit((AddNode4) node);
        } else {
            throw new IllegalArgumentException(String.valueOf(node));
        }
    }

    @Override public int visit(NumNode4 node) {
        return node.value;
    }

    @Override public int visit(AddNode4 node) {

        //compile error
        //return visit(node.left) + visit(node.left);

        if (node.left instanceof NumNode4) {
            NumNode4 left = (NumNode4) node.left;
            if (node.right instanceof NumNode4) {
                NumNode4 right = (NumNode4) node.right;
                return visit(left) + visit(right);
            } else if (node.right instanceof AddNode4) {
                AddNode4 right = (AddNode4) node.right;
                return visit(left) + visit(right);
            } else {
                throw new IllegalArgumentException(String.valueOf(node));
            }
        } else if (node.left instanceof AddNode4) {
            AddNode4 left = (AddNode4) node.left;
            if (node.right instanceof NumNode4) {
                NumNode4 right = (NumNode4) node.right;
                return visit(left) + visit(right);
            } else if (node.right instanceof AddNode4) {
                AddNode4 right = (AddNode4) node.right;
                return visit(left) + visit(right);
            } else {
                throw new IllegalArgumentException(String.valueOf(node));
            }
        } else {
            throw new IllegalArgumentException(String.valueOf(node));
        }
    }
}
