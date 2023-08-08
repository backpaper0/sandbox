package main

type NFARulebook struct {
	rules []*FARule
}

func NewNFARulebook(rules []*FARule) *NFARulebook {
	return &NFARulebook{
		rules: rules,
	}
}

func (rulebook *NFARulebook) NextStates(states *Set, character rune) *Set {
	nextStates := NewSet()
	for _, state := range states.Values() {
		nextStates.AddAll(rulebook.FollowRulesFor(state, character))
	}
	return nextStates
}

func (rulebook *NFARulebook) FollowRulesFor(state State, character rune) *Set {
	nextStates := NewSet()
	for _, rule := range rulebook.RulesFor(state, character) {
		nextStates.Add(rule.Follow())
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

func (rulebook *NFARulebook) FollowFreeMoves(states *Set) *Set {
	moreStates := rulebook.NextStates(states, 0)
	if states.ContainsSet(moreStates) {
		return states
	}
	moreStates.AddAll(states)
	return rulebook.FollowFreeMoves(moreStates)
}

type NFA struct {
	currentStates *Set
	acceptStates  *Set
	rulebook      *NFARulebook
}

func NewNFA(currentStates *Set, acceptStates *Set, rulebook *NFARulebook) *NFA {
	return &NFA{
		currentStates: currentStates,
		acceptStates:  acceptStates,
		rulebook:      rulebook,
	}
}

func (nfa *NFA) getCurrentStates() *Set {
	return nfa.rulebook.FollowFreeMoves(nfa.currentStates)
}

func (nfa *NFA) IsAccepting() bool {
	for _, currentState := range nfa.getCurrentStates().Values() {
		if nfa.acceptStates.Contains(currentState) {
			return true
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
	acceptStates *Set
	rulebook     *NFARulebook
}

func NewNFADesign(startState State, acceptStates *Set, rulebook *NFARulebook) *NFADesign {
	return &NFADesign{
		startState:   startState,
		acceptStates: acceptStates,
		rulebook:     rulebook,
	}
}

func (design *NFADesign) ToNFA() *NFA {
	return design.ToNFAWith(NewSet(design.startState))
}

func (design *NFADesign) ToNFAWith(currentStates *Set) *NFA {
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

func (simulation *NFASimulation) NextState(states *Set, character rune) *Set {
	nfa := simulation.design.ToNFAWith(states)
	nfa.ReadCharacter(character)
	return nfa.getCurrentStates()
}
