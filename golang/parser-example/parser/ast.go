package parser

import "fmt"

type AstNode interface {
	accept(visitor AstNodeVisitor)
}

type AstNodeVisitor interface {
	visitScalarValue(node ScalarValue)
	visitBinalyOperation(node BinalyOperation)
}

type ScalarValue struct {
	Value Token
}

func (node ScalarValue) accept(visitor AstNodeVisitor) {
	visitor.visitScalarValue(node)
}

type BinalyOperator int

const (
	Add BinalyOperator = iota
	Sub
	Mul
	Div
)

func (bo BinalyOperator) String() string {
	switch bo {
	case Add:
		return "+"
	case Sub:
		return "-"
	case Mul:
		return "*"
	case Div:
		return "/"
	}
	return fmt.Sprintf("Unknown(%v)", int(bo))
}

type BinalyOperation struct {
	Op    BinalyOperator
	Left  AstNode
	Right AstNode
}

func (node BinalyOperation) accept(visitor AstNodeVisitor) {
	visitor.visitBinalyOperation(node)
}
