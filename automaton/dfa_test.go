package main

import (
	"strconv"
	"testing"
)

func createDFARulebookForTest() (*DFARulebook, *int, *int, *int) {
	v1, v2, v3 := 1, 2, 3
	p1, p2, p3 := &v1, &v2, &v3
	return NewDFARulebook([]*FARule{
		NewFARule(p1, 'a', p2),
		NewFARule(p1, 'b', p1),
		NewFARule(p2, 'a', p2),
		NewFARule(p2, 'b', p3),
		NewFARule(p3, 'a', p3),
		NewFARule(p3, 'b', p3),
	}), p1, p2, p3
}

func TestDFARulebookNextState(t *testing.T) {
	rulebook, p1, p2, p3 := createDFARulebookForTest()
	fixtures := []struct {
		state     State
		character rune
		nextState State
	}{
		{p1, 'a', p2},
		{p1, 'b', p1},
		{p2, 'b', p3},
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
	rulebook, p1, _, p3 := createDFARulebookForTest()
	fixtures := []struct {
		currentState State
		acceptStates *Set
		accepted     bool
	}{
		{p1, NewSet(p1, p3), true},
		{p1, NewSet(p3), false},
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
	rulebook, p1, _, p3 := createDFARulebookForTest()
	dfa := NewDFA(p1, NewSet(p3), rulebook)
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
	rulebook, p1, _, p3 := createDFARulebookForTest()
	dfa := NewDFA(p1, NewSet(p3), rulebook)
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
	rulebook, p1, _, p3 := createDFARulebookForTest()
	dd := NewDFADesign(p1, NewSet(p3), rulebook)
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
