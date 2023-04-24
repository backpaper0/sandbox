package parser

import (
	"strconv"
)

func Evaluate(astNode AstNode) (int, error) {
	visitor := &Calculator{}
	return astNode.accept(visitor)
}

type Calculator struct {
	// stack *list.List
}

// func (c *Calculator) push(value int) {
// 	c.stack.PushBack(value)
// }

// func (c *Calculator) pop() (int, error) {
// 	element := c.stack.Back()
// 	if element == nil {
// 		return 0, errors.New("スタックが空です")
// 	}
// 	value := c.stack.Remove(element).(int)
// 	return value, nil
// }

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
