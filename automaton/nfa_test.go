package main

import (
	"reflect"
	"sort"
	"strconv"
	"testing"
)

func createNFARulebookForTest() *NFARulebook {
	return NewNFARulebook([]*FARule{
		NewFARule(1, 'a', 1),
		NewFARule(1, 'b', 1),
		NewFARule(1, 'b', 2),
		NewFARule(2, 'a', 3),
		NewFARule(2, 'b', 3),
		NewFARule(3, 'a', 4),
		NewFARule(3, 'b', 4),
	})
}

func createNFARulebookWithFreeMoveForTest() *NFARulebook {
	return NewNFARulebook([]*FARule{
		NewFARule(1, 0, 2),
		NewFARule(1, 0, 4),
		NewFARule(2, 'a', 3),
		NewFARule(3, 'a', 2),
		NewFARule(4, 'a', 5),
		NewFARule(5, 'a', 6),
		NewFARule(6, 'a', 4),
	})
}

func TestNFARulebookNextStates(t *testing.T) {
	rulebook := createNFARulebookForTest()
	fixtures := []struct {
		states     []State
		character  rune
		nextStates []State
	}{
		{[]State{1}, 'b', []State{1, 2}},
		{[]State{1, 2}, 'a', []State{1, 3}},
		{[]State{1, 3}, 'b', []State{1, 2, 4}},
	}
	for i, f := range fixtures {
		t.Run("TestNFARulebookNextStates"+strconv.Itoa(i), func(t *testing.T) {
			nextStates := rulebook.NextStates(f.states, f.character)
			if !reflect.DeepEqual(nextStates, f.nextStates) {
				t.Errorf("Expected is %v but actual is %v", f.nextStates, nextStates)
			}
		})
	}
}

func TestNFAIsAccepting(t *testing.T) {
	rulebook := createNFARulebookForTest()
	fixtures := []struct {
		currentStates []State
		acceptStates  []State
		isAccepting   bool
	}{
		{[]State{1}, []State{4}, false},
		{[]State{1, 2, 4}, []State{4}, true},
	}
	for i, f := range fixtures {
		t.Run("TestNFAIsAccepting"+strconv.Itoa(i), func(t *testing.T) {
			nfa := NewNFA(f.currentStates, f.acceptStates, rulebook)
			isAccepting := nfa.IsAccepting()
			if isAccepting != f.isAccepting {
				t.Error()
			}
		})
	}
}

func TestNFAReadCharacter(t *testing.T) {
	rulebook := createNFARulebookForTest()
	nfa := NewNFA([]State{1}, []State{4}, rulebook)
	if nfa.IsAccepting() != false {
		t.Error()
		return
	}
	nfa.ReadCharacter('b')
	if nfa.IsAccepting() != false {
		t.Error()
		return
	}
	nfa.ReadCharacter('a')
	if nfa.IsAccepting() != false {
		t.Error()
		return
	}
	nfa.ReadCharacter('b')
	if nfa.IsAccepting() != true {
		t.Error()
		return
	}
}

func TestNFAReadString(t *testing.T) {
	rulebook := createNFARulebookForTest()
	nfa := NewNFA([]State{1}, []State{4}, rulebook)
	if nfa.IsAccepting() != false {
		t.Error()
		return
	}
	nfa.ReadString("bbbbb")
	if nfa.IsAccepting() != true {
		t.Error()
		return
	}
}

func TestNFADesignAccepts(t *testing.T) {
	rulebook := createNFARulebookForTest()
	dd := NewNFADesign(1, []State{4}, rulebook)
	fixtures := []struct {
		text    string
		accepts bool
	}{
		{"bab", true},
		{"bbbbb", true},
		{"bbabb", false},
	}
	for i, f := range fixtures {
		t.Run("TestDFADesignAccepts"+strconv.Itoa(i), func(t *testing.T) {
			if dd.Accepts(f.text) != f.accepts {
				t.Error()
			}
		})
	}
}

func TestNFARulebookNextMove(t *testing.T) {
	rulebook := createNFARulebookWithFreeMoveForTest()
	nextStates := rulebook.NextStates([]State{1}, 0)
	expected := []State{2, 4}
	if !reflect.DeepEqual(nextStates, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, nextStates)
	}
}

func TestNFARulebookFollowFreeMoves(t *testing.T) {
	rulebook := createNFARulebookWithFreeMoveForTest()
	nextStates := rulebook.FollowFreeMoves([]State{1})
	sort.Slice(nextStates, func(i, j int) bool {
		return nextStates[i] < nextStates[j]
	})
	expected := []State{1, 2, 4}
	if !reflect.DeepEqual(nextStates, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, nextStates)
	}
}

func TestNFADesignAcceptsWithFreeMove(t *testing.T) {
	rulebook := createNFARulebookWithFreeMoveForTest()
	dd := NewNFADesign(1, []State{2, 4}, rulebook)
	fixtures := []struct {
		text    string
		accepts bool
	}{
		{"aa", true},
		{"aaa", true},
		{"aaaaa", false},
		{"aaaaaa", true},
	}
	for i, f := range fixtures {
		t.Run("TestDFADesignAccepts"+strconv.Itoa(i), func(t *testing.T) {
			if dd.Accepts(f.text) != f.accepts {
				t.Error()
			}
		})
	}
}
