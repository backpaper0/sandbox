package parser.combinator.example;

public class ExpressionNode {

	public static class Addition extends ExpressionNode {

		private final ExpressionNode left;
		private final ExpressionNode right;

		public Addition(final ExpressionNode left, final ExpressionNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return String.format("(%s + %s)", left, right);
		}
	}

	public static class Subtraction extends ExpressionNode {

		private final ExpressionNode left;
		private final ExpressionNode right;

		public Subtraction(final ExpressionNode left, final ExpressionNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return String.format("(%s - %s)", left, right);
		}
	}

	public static class Multiplication extends ExpressionNode {

		private final ExpressionNode left;
		private final ExpressionNode right;

		public Multiplication(final ExpressionNode left, final ExpressionNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return String.format("(%s * %s)", left, right);
		}
	}

	public static class Division extends ExpressionNode {

		private final ExpressionNode left;
		private final ExpressionNode right;

		public Division(final ExpressionNode left, final ExpressionNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return String.format("(%s / %s)", left, right);
		}
	}

	public static class ValueNode extends ExpressionNode {

		private final int value;

		public ValueNode(final int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
}