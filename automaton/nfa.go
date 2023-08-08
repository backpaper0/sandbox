package main

type NFARulebook struct {
	rules []*FARule
}

func NewNFARulebook(rules []*FARule) *NFARulebook {
	return &NFARulebook{
		rules: rules,
	}
}

func (rulebook *NFARulebook) NextStates(states []State, character rune) []State {
	nextStates := make([]State, 0)
	for _, state := range states {
		nextStates = append(nextStates, rulebook.FollowRulesFor(state, character)...)
	}
	return nextStates
}

func (rulebook *NFARulebook) FollowRulesFor(state State, character rune) []State {
	nextStates := make([]State, 0)
	for _, rule := range rulebook.RulesFor(state, character) {
		nextStates = append(nextStates, rule.Follow())
	}
	return nextStates
}

func (rulebook *NFARulebook) RulesFor(state State, character rune) []*FARule {
	rules := make([]*FARule, 0)
	for _, rule := range rulebook.rules {
		if rule.IsAppliesTo(state, character) {
			rules = append(rules, rule)
		}
	}
	return rules
}

func (rulebook *NFARulebook) FollowFreeMoves(states []State) []State {
	isSubset := func(as, bs []State) bool {
		for _, a := range as {
			result := false
			for _, b := range bs {
				if a == b {
					result = true
					break
				}
			}
			if !result {
				return false
			}
		}
		return true
	}
	moreStates := rulebook.NextStates(states, 0)
	if isSubset(moreStates, states) {
		return states
	}
	return rulebook.FollowFreeMoves(append(moreStates, states...))
}

type NFA struct {
	currentStates []State
	acceptStates  []State
	rulebook      *NFARulebook
}

func NewNFA(currentStates []State, acceptStates []State, rulebook *NFARulebook) *NFA {
	return &NFA{
		currentStates: currentStates,
		acceptStates:  acceptStates,
		rulebook:      rulebook,
	}
}

func (nfa *NFA) getCurrentStates() []State {
	return nfa.rulebook.FollowFreeMoves(nfa.currentStates)
}

func (nfa *NFA) IsAccepting() bool {
	for _, currentState := range nfa.getCurrentStates() {
		for _, acceptState := range nfa.acceptStates {
			if currentState == acceptState {
				return true
			}
		}
	}
	return false
}

func (nfa *NFA) ReadCharacter(character rune) {
	nfa.currentStates = nfa.rulebook.NextStates(nfa.getCurrentStates(), character)
}

func (nfa *NFA) ReadString(text string) {
	for _, character := range text {
		nfa.ReadCharacter(character)
	}
}

type NFADesign struct {
	startState   State
	acceptStates []State
	rulebook     *NFARulebook
}

func NewNFADesign(startState State, acceptStates []State, rulebook *NFARulebook) *NFADesign {
	return &NFADesign{
		startState:   startState,
		acceptStates: acceptStates,
		rulebook:     rulebook,
	}
}

func (design *NFADesign) ToNFA() *NFA {
	return design.ToNFAWith([]State{design.startState})
}

func (design *NFADesign) ToNFAWith(currentStates []State) *NFA {
	return NewNFA(currentStates, design.acceptStates, design.rulebook)
}

func (design *NFADesign) Accepts(text string) bool {
	nfa := design.ToNFA()
	nfa.ReadString(text)
	return nfa.IsAccepting()
}

type NFASimulation struct {
	design *NFADesign
}

func NewNFASimulation(design *NFADesign) *NFASimulation {
	return &NFASimulation{
		design: design,
	}
}

func (simulation *NFASimulation) NextState(states []State, character rune) []State {
	nfa := simulation.design.ToNFAWith(states)
	nfa.ReadCharacter(character)
	return nfa.getCurrentStates()
}
