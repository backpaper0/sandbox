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
		t.Run(fmt.Sprintf("TestPatternLiteralToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}

func TestPatternConcatenateToNFADesign(t *testing.T) {
	design := NewConcatenate(NewLiteral('a'), NewLiteral('b')).ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"a", false},
		{"ab", true},
		{"abc", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternConcatenateToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}

func TestPatternChooseToNFADesign(t *testing.T) {
	design := NewChoose(NewLiteral('a'), NewLiteral('b')).ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"a", true},
		{"b", true},
		{"c", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternChooseToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}

func TestPatternRepeatToNFADesign(t *testing.T) {
	design := NewRepeat(NewLiteral('a')).ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"", true},
		{"a", true},
		{"aaaa", true},
		{"b", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternRepeatToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}

func TestPatternToNFADesign(t *testing.T) {
	// (a(|b))*
	design := NewRepeat(
		NewConcatenate(
			NewLiteral('a'),
			NewChoose(NewEmpty(), NewLiteral('b')),
		),
	).ToNFADesign()
	fixtures := []struct {
		text     string
		accepted bool
	}{
		{"", true},
		{"a", true},
		{"ab", true},
		{"aba", true},
		{"abab", true},
		{"abaab", true},
		{"abba", false},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestPatternRepeatToNFADesign%v", i), func(t *testing.T) {
			accepted := design.Accepts(f.text)
			if accepted != f.accepted {
				t.Error()
			}
		})
	}
}
