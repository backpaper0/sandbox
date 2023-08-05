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

func (rule *FARule) IsAppliesTo(state int, character rune) bool {
	return rule.state == state && rule.character == character
}

func (rule *FARule) Follow() int {
	return rule.nextState
}

func (rule *FARule) String() string {
	return "#<FARule " + strconv.Itoa(rule.state) + " --" + string(rule.character) + "--> " + strconv.Itoa(rule.nextState)
}

type DFARulebook struct {
	rules []*FARule
}

func NewDFARulebook(rules []*FARule) *DFARulebook {
	return &DFARulebook{
		rules: rules,
	}
}

func (rulebook *DFARulebook) NextState(state int, character rune) int {
	return rulebook.RuleFor(state, character).Follow()
}

func (rulebook *DFARulebook) RuleFor(state int, character rune) *FARule {
	for _, r := range rulebook.rules {
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

func (design *DFADesign) ToDFA() *DFA {
	return NewDFA(design.startState, design.acceptStates, design.rulebook)
}

func (design *DFADesign) Accepts(text string) bool {
	dfa := design.ToDFA()
	dfa.ReadString(text)
	return dfa.IsAccepting()
}
