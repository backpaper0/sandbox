package main

import (
	"strconv"
)

type FARule struct {
	state     int
	character rune
	nextState int
}

func NewFARule(state int, character rune, nextState int) *FARule {
	return &FARule{
		state:     state,
		character: character,
		nextState: nextState,
	}
}

func (r *FARule) IsAppliesTo(state int, character rune) bool {
	return r.state == state && r.character == character
}

func (r *FARule) Follow() int {
	return r.nextState
}

func (r *FARule) String() string {
	return "#<FARule " + strconv.Itoa(r.state) + " --" + string(r.character) + "--> " + strconv.Itoa(r.nextState)
}

type DFARulebook struct {
	rules []*FARule
}

func NewDFARulebook(rules []*FARule) *DFARulebook {
	return &DFARulebook{
		rules: rules,
	}
}

func (rb *DFARulebook) NextState(state int, character rune) int {
	return rb.RuleFor(state, character).Follow()
}

func (rb *DFARulebook) RuleFor(state int, character rune) *FARule {
	for _, r := range rb.rules {
		if r.IsAppliesTo(state, character) {
			return r
		}
	}
	return nil
}

type DFA struct {
	currentState int
	acceptStates []int
	rulebook     *DFARulebook
}

func NewDFA(currentState int, acceptStates []int, rulebook *DFARulebook) *DFA {
	return &DFA{
		currentState: currentState,
		acceptStates: acceptStates,
		rulebook:     rulebook,
	}
}

func (dfa *DFA) IsAccepting() bool {
	for _, as := range dfa.acceptStates {
		if as == dfa.currentState {
			return true
		}
	}
	return false
}

func (dfa *DFA) ReadCharacter(character rune) {
	dfa.currentState = dfa.rulebook.NextState(dfa.currentState, character)
}

func (dfa *DFA) ReadString(text string) {
	for _, character := range text {
		dfa.ReadCharacter(character)
	}
}

type DFADesign struct {
	startState   int
	acceptStates []int
	rulebook     *DFARulebook
}

func NewDFADesign(startState int, acceptStates []int, rulebook *DFARulebook) *DFADesign {
	return &DFADesign{
		startState:   startState,
		acceptStates: acceptStates,
		rulebook:     rulebook,
	}
}

func (dd *DFADesign) ToDFA() *DFA {
	return NewDFA(dd.startState, dd.acceptStates, dd.rulebook)
}

func (dd *DFADesign) Accepts(text string) bool {
	dfa := dd.ToDFA()
	dfa.ReadString(text)
	return dfa.IsAccepting()
}
