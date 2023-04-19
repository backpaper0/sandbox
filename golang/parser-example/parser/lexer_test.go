package parser

import (
	"testing"
)

func TestNextToken(t *testing.T) {

	fixtures := []struct {
		Expr     string
		Expected []Token
	}{
		{"1", []Token{{Integer, "1"}, {Eof, ""}}},
		{"2", []Token{{Integer, "2"}, {Eof, ""}}},
		{"123", []Token{{Integer, "123"}, {Eof, ""}}},
		{"+", []Token{{AddOp, "+"}, {Eof, ""}}},
		{"-", []Token{{SubOp, "-"}, {Eof, ""}}},
		{"*", []Token{{MulOp, "*"}, {Eof, ""}}},
		{"/", []Token{{DivOp, "/"}, {Eof, ""}}},
		{"(", []Token{{LeftBracket, "("}, {Eof, ""}}},
		{")", []Token{{RightBracket, ")"}, {Eof, ""}}},
		{"1 + 2", []Token{{Integer, "1"}, {AddOp, "+"}, {Integer, "2"}, {Eof, ""}}},
		{"(1 + 2) * (3 + 4)", []Token{
			{LeftBracket, "("}, {Integer, "1"}, {AddOp, "+"}, {Integer, "2"}, {RightBracket, ")"},
			{MulOp, "*"},
			{LeftBracket, "("}, {Integer, "3"}, {AddOp, "+"}, {Integer, "4"}, {RightBracket, ")"},
			{Eof, ""}}},
	}

	for _, fixture := range fixtures {
		t.Run(fixture.Expr, func(t *testing.T) {
			lexer := NewLexer(fixture.Expr)
			for _, expected := range fixture.Expected {
				actual, err := lexer.NextToken()
				if actual != expected {
					t.Errorf("Expected is %v but actual is %v", expected, actual)
				} else if err != nil {
					t.Error(err)
				}
			}
		})
	}
}

func TestNextTokenError(t *testing.T) {

	fixtures := []string{
		".",
		"x",
	}

	for _, fixture := range fixtures {
		t.Run(fixture, func(t *testing.T) {
			lexer := NewLexer(fixture)
			_, err := lexer.NextToken()
			if err == nil {
				t.Fail()
			} else if err.Error() != "invalid token: "+fixture {
				t.Error(err)
			}
		})
	}
}
