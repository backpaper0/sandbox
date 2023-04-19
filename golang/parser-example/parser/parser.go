package parser

import "fmt"

type Parser struct {
	lexer *Lexer
	token Token
}

func NewParser(expr string) (*Parser, error) {
	lexer := NewLexer(expr)
	parser := &Parser{lexer, Token{}}
	err := parser.consume()
	return parser, err
}

func (parser *Parser) consume() error {
	token, err := parser.lexer.NextToken()
	if err == nil {
		parser.token = token
	}
	return err
}

func (parser *Parser) match(tokenType TokenType) error {
	if parser.token.Type == tokenType {
		return parser.consume()
	}
	return fmt.Errorf("expected is %v but actual is %v", tokenType, parser.token.Type)
}

func (parser *Parser) Parse() (AstNode, error) {
	node, err := parser.expr()
	if err != nil {
		return nil, err
	}
	err = parser.match(Eof)
	return node, err
}

func (parser *Parser) expr() (AstNode, error) {
	node, err := parser.term()
	if err != nil {
		return nil, err
	}
	for parser.token.Type == AddOp || parser.token.Type == SubOp {
		tokenTypeShelter := parser.token.Type
		parser.consume() // 演算子を消費する
		rightNode, err := parser.term()
		if err != nil {
			return nil, err
		}
		switch tokenTypeShelter {
		case AddOp:
			return BinalyOperation{Add, node, rightNode}, nil
		case SubOp:
			return BinalyOperation{Sub, node, rightNode}, nil
		}
	}
	return node, nil
}

func (parser *Parser) term() (AstNode, error) {
	node, err := parser.factor()
	if err != nil {
		return nil, err
	}
	for parser.token.Type == MulOp || parser.token.Type == DivOp {
		tokenTypeShelter := parser.token.Type
		parser.consume() // 演算子を消費する
		rightNode, err := parser.factor()
		if err != nil {
			return nil, err
		}
		switch tokenTypeShelter {
		case MulOp:
			return BinalyOperation{Mul, node, rightNode}, nil
		case DivOp:
			return BinalyOperation{Div, node, rightNode}, nil
		}
	}
	return node, nil
}

func (parser *Parser) factor() (AstNode, error) {
	if parser.token.Type != LeftBracket {
		return parser.number()
	}
	err := parser.match(LeftBracket)
	if err != nil {
		return nil, err
	}
	node, err := parser.expr()
	if err != nil {
		return nil, err
	}
	err = parser.match(RightBracket)
	if err != nil {
		return nil, err
	}
	return node, nil
}

func (parser *Parser) number() (AstNode, error) {
	tokenShelter := parser.token
	err := parser.match(Integer)
	if err != nil {
		return nil, err
	}
	node := ScalarValue{tokenShelter}
	return node, nil
}
