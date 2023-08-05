package main

import (
	"strconv"
	"testing"
)

func createDFARulebookForTest() *DFARulebook {
	return NewDFARulebook([]*FARule{
		NewFARule(1, 'a', 2),
		NewFARule(1, 'b', 1),
		NewFARule(2, 'a', 2),
		NewFARule(2, 'b', 3),
		NewFARule(3, 'a', 3),
		NewFARule(3, 'b', 3),
	})
}

func TestDFARulebookNextState(t *testing.T) {
	rulebook := createDFARulebookForTest()
	fixtures := []struct {
		state     int
		character rune
		nextState int
	}{
		{1, 'a', 2},
		{1, 'b', 1},
		{2, 'b', 3},
	}
	for i, f := range fixtures {
		t.Run("TestDFARulebookNextState"+strconv.Itoa(i), func(t *testing.T) {
			nextState := rulebook.NextState(f.state, f.character)
			if nextState != f.nextState {
				t.Errorf("Expected is %v but actual is %v", f.nextState, nextState)
			}
		})
	}
}

func TestDFAIsAccepting(t *testing.T) {
	rulebook := createDFARulebookForTest()
	fixtures := []struct {
		currentState int
		acceptStates []int
		accepted     bool
	}{
		{1, []int{1, 3}, true},
		{1, []int{3}, false},
	}
	for i, f := range fixtures {
		t.Run("TestDFAIsAccepting"+strconv.Itoa(i), func(t *testing.T) {
			dfa := NewDFA(f.currentState, f.acceptStates, rulebook)
			accepted := dfa.IsAccepting()
			if accepted != f.accepted {
				t.Errorf("Expected is %v but actual is %v", f.accepted, accepted)
			}
		})
	}
}

func TestDFAReadCharacter(t *testing.T) {
	rulebook := createDFARulebookForTest()
	dfa := NewDFA(1, []int{3}, rulebook)
	if dfa.IsAccepting() != false {
		t.Error()
		return
	}
	dfa.ReadCharacter('b')
	if dfa.IsAccepting() != false {
		t.Error()
		return
	}
	for i := 0; i < 3; i++ {
		dfa.ReadCharacter('a')
	}
	if dfa.IsAccepting() != false {
		t.Error()
		return
	}
	dfa.ReadCharacter('b')
	if dfa.IsAccepting() != true {
		t.Error()
		return
	}
}

func TestDFAReadString(t *testing.T) {
	rulebook := createDFARulebookForTest()
	dfa := NewDFA(1, []int{3}, rulebook)
	if dfa.IsAccepting() != false {
		t.Error()
		return
	}
	dfa.ReadString("baaab")
	if dfa.IsAccepting() != true {
		t.Error()
		return
	}
}

func TestDFADesignAccepts(t *testing.T) {
	rulebook := createDFARulebookForTest()
	dd := NewDFADesign(1, []int{3}, rulebook)
	fixtures := []struct {
		text    string
		accepts bool
	}{
		{"a", false},
		{"baa", false},
		{"baba", true},
	}
	for i, f := range fixtures {
		t.Run("TestDFADesignAccepts"+strconv.Itoa(i), func(t *testing.T) {
			if dd.Accepts(f.text) != f.accepts {
				t.Error()
			}
		})
	}
}
