package parser

import "testing"

func TestParse(t *testing.T) {

	fixtures := []struct {
		input    string
		expected AstNode
	}{
		{"0", ScalarValue{Token{Integer, "0"}}},
		{"1", ScalarValue{Token{Integer, "1"}}},
		{"12", ScalarValue{Token{Integer, "12"}}},
		{"98765", ScalarValue{Token{Integer, "98765"}}},
		{"1 + 2", BinalyOperation{Add, ScalarValue{Token{Integer, "1"}}, ScalarValue{Token{Integer, "2"}}}},
		{"(40 / 20) * (30 - 10)", BinalyOperation{Mul,
			BinalyOperation{Div, ScalarValue{Token{Integer, "40"}}, ScalarValue{Token{Integer, "20"}}},
			BinalyOperation{Sub, ScalarValue{Token{Integer, "30"}}, ScalarValue{Token{Integer, "10"}}}}},
	}

	for _, fixture := range fixtures {
		t.Run(fixture.input, func(t *testing.T) {
			prsr, err := NewParser(fixture.input)
			if err != nil {
				t.Error(err)
				return
			}
			actual, err := prsr.Parse()
			if err != nil {
				t.Error(err)
				return
			} else if actual != fixture.expected {
				t.Errorf("expected is %v but actual is %v", fixture.expected, actual)
				return
			}
		})
	}
}

func TestParseError(t *testing.T) {
	fixtures := []string{
		"00",
		"+",
		"1 + - 2",
		" ",
		"()",
	}
	for _, fixture := range fixtures {
		t.Run(fixture, func(t *testing.T) {
			var err1, err2 error
			prsr, err1 := NewParser(fixture)
			if err1 == nil {
				_, err2 = prsr.Parse()
			}
			if err1 == nil && err2 == nil {
				t.Fail()
				return
			}
			t.Log(err1, err2)
		})
	}
}
