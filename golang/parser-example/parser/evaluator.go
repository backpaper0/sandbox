package parser

import (
	"strconv"
)

func Evaluate(astNode AstNode) (int, error) {
	visitor := &Caluclator{}
	return astNode.accept(visitor)
}

type Caluclator struct {
	// stack *list.List
}

// func (c *Caluclator) push(value int) {
// 	c.stack.PushBack(value)
// }

// func (c *Caluclator) pop() (int, error) {
// 	element := c.stack.Back()
// 	if element == nil {
// 		return 0, errors.New("スタックが空です")
// 	}
// 	value := c.stack.Remove(element).(int)
// 	return value, nil
// }

func (c *Caluclator) visitScalarValue(node ScalarValue) (int, error) {
	return strconv.Atoi(node.Value.Value)
}

func (c *Caluclator) visitBinalyOperation(node BinalyOperation) (int, error) {
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
