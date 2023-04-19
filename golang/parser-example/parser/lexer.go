package parser

import (
	"fmt"
	"strings"
)

type Lexer struct {
	expr  string
	index int
	char  string
	eof   bool
}

type TokenType int

const (
	Integer TokenType = iota
	AddOp
	SubOp
	MulOp
	DivOp
	LeftBracket
	RightBracket
	Eof
)

func (tt TokenType) String() string {
	switch tt {
	case Integer:
		return "Integer"
	case AddOp:
		return "AddOp"
	case SubOp:
		return "SubOp"
	case MulOp:
		return "MulOp"
	case DivOp:
		return "DivOp"
	case LeftBracket:
		return "LeftBracket"
	case RightBracket:
		return "RightBracket"
	case Eof:
		return "Eof"
	}
	return fmt.Sprintf("Unknown(%v)", int(tt))
}

type Token struct {
	Type  TokenType
	Value string
}

func NewLexer(expr string) *Lexer {
	lexer := &Lexer{expr, 0, "", false}
	lexer.index = -1
	lexer.consume()
	return lexer
}

func (lexer *Lexer) consume() {
	if lexer.index < len(lexer.expr) {
		lexer.index++
	}
	if lexer.index < len(lexer.expr) {
		lexer.char = lexer.expr[lexer.index : lexer.index+1]
	} else {
		lexer.char = ""
		lexer.eof = true
	}
}

func (lexer *Lexer) NextToken() (Token, error) {
	for lexer.char == " " {
		lexer.consume()
	}
	if lexer.char == "+" {
		lexer.consume()
		return Token{AddOp, "+"}, nil
	} else if lexer.char == "-" {
		lexer.consume()
		return Token{SubOp, "-"}, nil
	} else if lexer.char == "*" {
		lexer.consume()
		return Token{MulOp, "*"}, nil
	} else if lexer.char == "/" {
		lexer.consume()
		return Token{DivOp, "/"}, nil
	} else if lexer.char == "(" {
		lexer.consume()
		return Token{LeftBracket, "("}, nil
	} else if lexer.char == ")" {
		lexer.consume()
		return Token{RightBracket, ")"}, nil
	} else if lexer.char == "0" {
		lexer.consume()
		return Token{Integer, "0"}, nil
	} else if "1" <= lexer.char && lexer.char <= "9" {
		builder := strings.Builder{}
		for {
			builder.WriteString(lexer.char)
			lexer.consume()
			if !("0" <= lexer.char && lexer.char <= "9") {
				break
			}
		}
		return Token{Integer, builder.String()}, nil
	} else if lexer.char == "" {
		return Token{Eof, ""}, nil
	}
	return Token{}, fmt.Errorf("invalid token: %v", lexer.char)
}
