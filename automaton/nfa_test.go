package main

import (
	"fmt"
	"reflect"
	"sort"
	"strconv"
	"testing"
)

func createNFARulebookForTest() (*NFARulebook, *int, *int, *int, *int) {
	v1, v2, v3, v4 := 1, 2, 3, 4
	p1, p2, p3, p4 := &v1, &v2, &v3, &v4
	return NewNFARulebook([]*FARule{
		NewFARule(p1, 'a', p1),
		NewFARule(p1, 'b', p1),
		NewFARule(p1, 'b', p2),
		NewFARule(p2, 'a', p3),
		NewFARule(p2, 'b', p3),
		NewFARule(p3, 'a', p4),
		NewFARule(p3, 'b', p4),
	}), p1, p2, p3, p4
}

func createNFARulebookWithFreeMoveForTest() (*NFARulebook, *int, *int, *int, *int, *int, *int) {
	v1, v2, v3, v4, v5, v6 := 1, 2, 3, 4, 5, 6
	p1, p2, p3, p4, p5, p6 := &v1, &v2, &v3, &v4, &v5, &v6
	return NewNFARulebook([]*FARule{
		NewFARule(p1, 0, p2),
		NewFARule(p1, 0, p4),
		NewFARule(p2, 'a', p3),
		NewFARule(p3, 'a', p2),
		NewFARule(p4, 'a', p5),
		NewFARule(p5, 'a', p6),
		NewFARule(p6, 'a', p4),
	}), p1, p2, p3, p4, p5, p6
}

func TestNFARulebookNextStates(t *testing.T) {
	rulebook, p1, p2, p3, p4 := createNFARulebookForTest()
	fixtures := []struct {
		states     []State
		character  rune
		nextStates []State
	}{
		{[]State{p1}, 'b', []State{p1, p2}},
		{[]State{p1, p2}, 'a', []State{p1, p3}},
		{[]State{p1, p3}, 'b', []State{p1, p2, p4}},
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
	rulebook, p1, p2, _, p4 := createNFARulebookForTest()
	fixtures := []struct {
		currentStates []State
		acceptStates  []State
		isAccepting   bool
	}{
		{[]State{p1}, []State{p4}, false},
		{[]State{p1, p2, p4}, []State{p4}, true},
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
	rulebook, p1, _, _, p4 := createNFARulebookForTest()
	nfa := NewNFA([]State{p1}, []State{p4}, rulebook)
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
	rulebook, p1, _, _, p4 := createNFARulebookForTest()
	nfa := NewNFA([]State{p1}, []State{p4}, rulebook)
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
	rulebook, p1, _, _, p4 := createNFARulebookForTest()
	dd := NewNFADesign(p1, []State{p4}, rulebook)
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
	rulebook, p1, p2, _, p4, _, _ := createNFARulebookWithFreeMoveForTest()
	nextStates := rulebook.NextStates([]State{p1}, 0)
	expected := []State{p2, p4}
	if !reflect.DeepEqual(nextStates, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, nextStates)
	}
}

func TestNFARulebookFollowFreeMoves(t *testing.T) {
	rulebook, p1, p2, _, p4, _, _ := createNFARulebookWithFreeMoveForTest()
	nextStates := rulebook.FollowFreeMoves([]State{p1})
	sort.Slice(nextStates, func(i, j int) bool {
		return fmt.Sprintf("%v", nextStates[i]) < fmt.Sprintf("%v", nextStates[j])
	})
	expected := []State{p1, p2, p4}
	if !reflect.DeepEqual(nextStates, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, nextStates)
	}
}

func TestNFADesignAcceptsWithFreeMove(t *testing.T) {
	rulebook, p1, p2, _, p4, _, _ := createNFARulebookWithFreeMoveForTest()
	dd := NewNFADesign(p1, []State{p2, p4}, rulebook)
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
