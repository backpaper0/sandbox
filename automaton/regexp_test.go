package main

import (
	"fmt"
	"testing"
)

func TestPatternStrig(t *testing.T) {
	pattern := NewRepeat(
		NewChoose(
			NewConcatenate(NewLiteral('a'), NewLiteral('b')),
			NewLiteral('a'),
		),
	)
	s := pattern.String()
	expected := "(ab|a)"
	if s != expected {
		t.Errorf("Expected is %v but actual is %v", expected, s)
	}
}

func TestPatternEmptyToNFADesign(t *testing.T) {
	design := NewEmpty().ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"", true},
		{"a", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternEmptyToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}

func TestPatternLiteralToNFADesign(t *testing.T) {
	design := NewLiteral('a').ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"", false},
		{"a", true},
		{"b", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternEmptyToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}
