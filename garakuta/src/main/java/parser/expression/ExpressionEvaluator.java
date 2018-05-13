package parser.expression;

import parser.expression.ExpressionNode.Addition;
import parser.expression.ExpressionNode.Division;
import parser.expression.ExpressionNode.Multiplication;
import parser.expression.ExpressionNode.Subtraction;
import parser.expression.ExpressionNode.Value;

public class ExpressionEvaluator implements ExpressionNode.Visitor<Void, Integer> {

    public int evaluate(final ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public Integer visit(final Addition addition, final Void argument) {
        return addition.getLeft().accept(this, argument)
                + addition.getRight().accept(this, argument);
    }

    @Override
    public Integer visit(final Subtraction subtraction, final Void argument) {
        return subtraction.getLeft().accept(this, argument)
                - subtraction.getRight().accept(this, argument);
    }

    @Override
    public Integer visit(final Multiplication multiplication, final Void argument) {
        return multiplication.getLeft().accept(this, argument)
                * multiplication.getRight().accept(this, argument);
    }

    @Override
    public Integer visit(final Division division, final Void argument) {
        return division.getLeft().accept(this, argument)
                / division.getRight().accept(this, argument);
    }

    @Override
    public Integer visit(final Value value, final Void argument) {
        return value.getValue();
    }
}
