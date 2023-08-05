package main

import (
	"reflect"
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

func TestNFARulebookNextStates(t *testing.T) {
	rulebook := createNFARulebookForTest()
	fixtures := []struct {
		states     []int
		character  rune
		nextStates []int
	}{
		{[]int{1}, 'b', []int{1, 2}},
		{[]int{1, 2}, 'a', []int{1, 3}},
		{[]int{1, 3}, 'b', []int{1, 2, 4}},
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
		currentStates []int
		acceptStates  []int
		isAccepting   bool
	}{
		{[]int{1}, []int{4}, false},
		{[]int{1, 2, 4}, []int{4}, true},
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
	nfa := NewNFA([]int{1}, []int{4}, rulebook)
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
	nfa := NewNFA([]int{1}, []int{4}, rulebook)
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
	dd := NewNFADesign(1, []int{4}, rulebook)
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
