package parser

import (
	"strconv"
)

func Evaluate(astNode AstNode) (int, error) {
	visitor := &Calculator{}
	return astNode.accept(visitor)
}

type Calculator struct {
}

func (c *Calculator) visitScalarValue(node ScalarValue) (int, error) {
	return strconv.Atoi(node.Value.Value)
}

func (c *Calculator) visitBinalyOperation(node BinalyOperation) (int, error) {
	left, err := node.Left.accept(c)
	if err != nil {
		return 0, err
	}
	right, err := node.Right.accept(c)
	if err != nil {
		return 0, err
	}
	var value int
	switch node.Op {
	case Add:
		value = left + right
	case Sub:
		value = left - right
	case Mul:
		value = left * right
	case Div:
		value = left / right
	}
	return value, nil
}
