package parser

import "fmt"

type AstNode interface {
}

type ScalarValue struct {
	Value Token
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
