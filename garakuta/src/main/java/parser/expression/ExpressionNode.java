package parser.expression;

import java.util.Objects;

public abstract class ExpressionNode {

    public abstract <A, R> R accept(Visitor<A, R> visitor, A argument);

    public static ExpressionNode add(final ExpressionNode left, final ExpressionNode right) {
        return new Addition(left, right);
    }

    public static ExpressionNode sub(final ExpressionNode left, final ExpressionNode right) {
        return new Subtraction(left, right);
    }

    public static ExpressionNode mul(final ExpressionNode left, final ExpressionNode right) {
        return new Multiplication(left, right);
    }

    public static ExpressionNode div(final ExpressionNode left, final ExpressionNode right) {
        return new Division(left, right);
    }

    public static ExpressionNode val(final int value) {
        return new Value(value);
    }

    private static abstract class BinOp extends ExpressionNode {

        private final ExpressionNode left;
        private final ExpressionNode right;

        public BinOp(final ExpressionNode left, final ExpressionNode right) {
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
        }

        public ExpressionNode getLeft() {
            return left;
        }

        public ExpressionNode getRight() {
            return right;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj.getClass() != this.getClass()) {
                return false;
            }
            final BinOp other = (BinOp) obj;
            return left.equals(other.left) && right.equals(other.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(getClass(), left, right);
        }
    }

    public static class Addition extends BinOp {

        public Addition(final ExpressionNode left, final ExpressionNode right) {
            super(left, right);
        }

        @Override
        public <A, R> R accept(final Visitor<A, R> visitor, final A argument) {
            return visitor.visit(this, argument);
        }

        @Override
        public String toString() {
            return String.format("(%s + %s)", getLeft(), getRight());
        }
    }

    public static class Subtraction extends BinOp {

        public Subtraction(final ExpressionNode left, final ExpressionNode right) {
            super(left, right);
        }

        @Override
        public <A, R> R accept(final Visitor<A, R> visitor, final A argument) {
            return visitor.visit(this, argument);
        }

        @Override
        public String toString() {
            return String.format("(%s - %s)", getLeft(), getRight());
        }
    }

    public static class Multiplication extends BinOp {

        public Multiplication(final ExpressionNode left, final ExpressionNode right) {
            super(left, right);
        }

        @Override
        public <A, R> R accept(final Visitor<A, R> visitor, final A argument) {
            return visitor.visit(this, argument);
        }

        @Override
        public String toString() {
            return String.format("(%s * %s)", getLeft(), getRight());
        }
    }

    public static class Division extends BinOp {

        public Division(final ExpressionNode left, final ExpressionNode right) {
            super(left, right);
        }

        @Override
        public <A, R> R accept(final Visitor<A, R> visitor, final A argument) {
            return visitor.visit(this, argument);
        }

        @Override
        public String toString() {
            return String.format("(%s / %s)", getLeft(), getRight());
        }
    }

    public static class Value extends ExpressionNode {

        private final int value;

        public Value(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public <A, R> R accept(final Visitor<A, R> visitor, final A argument) {
            return visitor.visit(this, argument);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj.getClass() != this.getClass()) {
                return false;
            }
            final Value other = (Value) obj;
            return value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getClass(), value);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public interface Visitor<A, R> {

        R visit(Addition addition, A argument);

        R visit(Subtraction subtraction, A argument);

        R visit(Multiplication multiplication, A argument);

        R visit(Division division, A argument);

        R visit(Value value, A argument);
    }
}