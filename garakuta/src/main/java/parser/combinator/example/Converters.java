package parser.combinator.example;

import java.util.List;

import parser.combinator.Converter;

class Converters {

    static Converter operation() {
        return x -> {
            final List<Object> list = (List<Object>) x;
            ExpressionNode node = (ExpressionNode) list.get(0);
            for (final List<Object> list2 : (List<List<Object>>) list.get(1)) {
                final ExpressionNode node2 = (ExpressionNode) list2.get(1);
                switch ((String) list2.get(0)) {
                case "+":
                    node = new ExpressionNode.Addition(node, node2);
                    break;
                case "-":
                    node = new ExpressionNode.Subtraction(node, node2);
                    break;
                case "*":
                    node = new ExpressionNode.Multiplication(node, node2);
                    break;
                case "/":
                    node = new ExpressionNode.Division(node, node2);
                    break;
                }
            }
            return node;
        };
    }

    static Converter removeParen() {
        return x -> {
            final List<Object> list = (List<Object>) x;
            return list.get(1);
        };
    }

    static Converter zero() {
        return x -> new ExpressionNode.ValueNode(0);
    }

    static Converter digit() {
        return x -> Integer.parseInt((String) x);
    }

    static Converter integer() {
        return x -> {
            final List<Object> list = (List<Object>) x;
            int value = (int) list.get(0);
            for (final int i : (List<Integer>) list.get(1)) {
                value = value * 10 + i;
            }
            return new ExpressionNode.ValueNode(value);
        };
    }
}