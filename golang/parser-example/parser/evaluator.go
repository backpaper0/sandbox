package parser

import (
	"container/list"
	"strconv"
)

func Evaluate(astNode AstNode) int {
	visitor := &Caluclator{list.New()}
	astNode.accept(visitor)
	return visitor.pop()
}

type Caluclator struct {
	stack *list.List
}

func (c *Caluclator) push(value int) {
	c.stack.PushBack(value)
}

func (c *Caluclator) pop() int {
	element := c.stack.Back()
	if element == nil {
		//TODO スタックが空の場合
		return 0
	}
	value := c.stack.Remove(element).(int)
	return value
}

func (c *Caluclator) visitScalarValue(node ScalarValue) {
	value, _ /* TODO errの扱い */ := strconv.Atoi(node.Value.Value)
	c.push(value)
}

func (c *Caluclator) visitBinalyOperation(node BinalyOperation) {
	node.Left.accept(c)
	node.Right.accept(c)
	right := c.pop()
	left := c.pop()
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
	c.push(value)
}
