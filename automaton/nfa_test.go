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

func createNFADesignForTest() (*NFADesign, *int, *int, *int) {
	v1, v2, v3 := 1, 2, 3
	s1, s2, s3 := &v1, &v2, &v3
	rulebook := NewNFARulebook([]*FARule{
		NewFARule(s1, 'a', s1),
		NewFARule(s1, 'a', s2),
		NewFARule(s1, 0, s2),
		NewFARule(s2, 'b', s3),
		NewFARule(s3, 'b', s1),
		NewFARule(s3, 0, s2),
	})
	return NewNFADesign(s1, []State{s3}, rulebook), s1, s2, s3
}

func sortStates(states []State) {
	sort.Slice(states, func(i, j int) bool {
		return *states[i] < *states[j]
	})
}

func uniqueStates(states []State) []State {
	ss := make([]State, 0)
	contains := func(s State) bool {
		for _, t := range ss {
			if s == t {
				return true
			}
		}
		return false
	}
	for _, s := range states {
		if !contains(s) {
			ss = append(ss, s)
		}
	}
	return ss
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
	sortStates(nextStates)
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

func TestNFADesignToNFAWith(t *testing.T) {
	design, s1, s2, s3 := createNFADesignForTest()
	t.Run("TestNFADesignToNFAWith"+"1", func(t *testing.T) {
		currentStates := design.ToNFA().getCurrentStates()
		sortStates(currentStates)
		expected := []State{s1, s2}
		if !reflect.DeepEqual(currentStates, expected) {
			t.Error()
		}
	})
	t.Run("TestNFADesignToNFAWith"+"2", func(t *testing.T) {
		currentStates := design.ToNFAWith([]State{s2}).getCurrentStates()
		sortStates(currentStates)
		expected := []State{s2}
		if !reflect.DeepEqual(currentStates, expected) {
			t.Error()
		}
	})
	t.Run("TestNFADesignToNFAWith"+"3", func(t *testing.T) {
		currentStates := design.ToNFAWith([]State{s3}).getCurrentStates()
		sortStates(currentStates)
		expected := []State{s2, s3}
		if !reflect.DeepEqual(currentStates, expected) {
			t.Error()
		}
	})
}

func TestNFASimulationNextState(t *testing.T) {
	design, s1, s2, s3 := createNFADesignForTest()
	simulation := NewNFASimulation(design)
	fixtures := []struct {
		states    []State
		character rune
		expected  []State
	}{
		{[]State{s1, s2}, 'a', []State{s1, s2}},
		{[]State{s1, s2}, 'b', []State{s2, s3}},
		{[]State{s2, s3}, 'b', []State{s1, s2, s3}},
		{[]State{s1, s2, s3}, 'b', []State{s1, s2, s3}},
		{[]State{s1, s2, s3}, 'a', []State{s1, s2}},
	}
	for i, f := range fixtures {
		t.Run(fmt.Sprintf("TestNFASimulationNextState%v", i), func(t *testing.T) {
			actual := simulation.NextState(f.states, f.character)
			sortStates(actual)
			//TODO プロダクションコード側で重複排除したい。Sliceじゃなくてjava.util.Set的なもので保持すれば良さそう
			actual = uniqueStates(actual)
			if !reflect.DeepEqual(f.expected, actual) {
				t.Errorf("Expected is %v but actual is %v", f.expected, actual)
			}
		})
	}
}
