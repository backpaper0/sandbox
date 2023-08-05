package main

type NFARulebook struct {
	rules []*FARule
}

func NewNFARulebook(rules []*FARule) *NFARulebook {
	return &NFARulebook{
		rules: rules,
	}
}

func (rulebook *NFARulebook) NextStates(states []int, character rune) []int {
	nextStates := make([]int, 0)
	for _, state := range states {
		nextStates = append(nextStates, rulebook.FollowRulesFor(state, character)...)
	}
	return nextStates
}

func (rulebook *NFARulebook) FollowRulesFor(state int, character rune) []int {
	nextStates := make([]int, 0)
	for _, rule := range rulebook.RulesFor(state, character) {
		nextStates = append(nextStates, rule.Follow())
	}
	return nextStates
}

func (rulebook *NFARulebook) RulesFor(state int, character rune) []*FARule {
	rules := make([]*FARule, 0)
	for _, rule := range rulebook.rules {
		if rule.IsAppliesTo(state, character) {
			rules = append(rules, rule)
		}
	}
	return rules
}

func (rulebook *NFARulebook) FollowFreeMoves(states []int) []int {
	isSubset := func(as, bs []int) bool {
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
	currentStates []int
	acceptStates  []int
	rulebook      *NFARulebook
}

func NewNFA(currentStates []int, acceptStates []int, rulebook *NFARulebook) *NFA {
	return &NFA{
		currentStates: currentStates,
		acceptStates:  acceptStates,
		rulebook:      rulebook,
	}
}

func (nfa *NFA) getCurrentStates() []int {
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
	startState   int
	acceptStates []int
	rulebook     *NFARulebook
}

func NewNFADesign(startState int, acceptStates []int, rulebook *NFARulebook) *NFADesign {
	return &NFADesign{
		startState:   startState,
		acceptStates: acceptStates,
		rulebook:     rulebook,
	}
}

func (design *NFADesign) ToNFA() *NFA {
	return NewNFA([]int{design.startState}, design.acceptStates, design.rulebook)
}

func (design *NFADesign) Accepts(text string) bool {
	nfa := design.ToNFA()
	nfa.ReadString(text)
	return nfa.IsAccepting()
}
