package parser

import "testing"

func TestEvaluate(t *testing.T) {

	fixtures := []struct {
		expr     string
		node     AstNode
		expected int
	}{
		{"123", ScalarValue{Token{Integer, "123"}}, 123},
		{"12 + 34", BinalyOperation{Add, ScalarValue{Token{Integer, "12"}}, ScalarValue{Token{Integer, "34"}}}, 12 + 34},
		{"(9 - 5) * (6 / 3)", BinalyOperation{Mul,
			BinalyOperation{Sub, ScalarValue{Token{Integer, "9"}}, ScalarValue{Token{Integer, "5"}}},
			BinalyOperation{Div, ScalarValue{Token{Integer, "6"}}, ScalarValue{Token{Integer, "3"}}}},
			(9 - 5) * (6 / 3)},
	}

	for _, fixture := range fixtures {
		t.Run(fixture.expr, func(t *testing.T) {
			actual, err := Evaluate(fixture.node)
			if err != nil {
				t.Error(err)
			}
			if actual != fixture.expected {
				t.Errorf("Expected is %v but actual is %v", fixture.expected, actual)
			}
		})
	}
}
