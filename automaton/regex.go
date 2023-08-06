package main

type Pattern interface {
	String() string
	GetPrecedence() int
	ToNFADesign() *NFADesign
}

func bracket(pattern Pattern, otherPrecedence int) string {
	if pattern.GetPrecedence() < otherPrecedence {
		return "(" + pattern.String() + ")"
	}
	return pattern.String()
}

type Empty struct {
}

func NewEmpty() *Empty {
	return &Empty{}
}

func (empty *Empty) String() string {
	return ""
}

func (empty *Empty) GetPrecedence() int {
	return 3
}

func (empty *Empty) ToNFADesign() *NFADesign {
	v0 := 0
	p0 := &v0
	startState := State(p0)
	acceptStates := []State{startState}
	rulebook := NewNFARulebook([]*FARule{})
	return NewNFADesign(startState, acceptStates, rulebook)
}

type Literal struct {
	character rune
}

func NewLiteral(character rune) *Literal {
	return &Literal{
		character: character,
	}
}

func (literal *Literal) String() string {
	return string(literal.character)
}

func (literal *Literal) GetPrecedence() int {
	return 3
}

func (literal *Literal) ToNFADesign() *NFADesign {
	v0, v1 := 0, 0
	p0, p1 := &v0, &v1
	startState := State(p0)
	acceptState := State(p1)
	rule := NewFARule(startState, literal.character, acceptState)
	rulebook := NewNFARulebook([]*FARule{rule})
	return NewNFADesign(startState, []State{acceptState}, rulebook)
}

type Concatenate struct {
	first, second Pattern
}

func NewConcatenate(first, second Pattern) *Concatenate {
	return &Concatenate{
		first:  first,
		second: second,
	}
}

func (concatenate *Concatenate) String() string {
	return bracket(concatenate.first, concatenate.GetPrecedence()) + bracket(concatenate.second, concatenate.GetPrecedence())
}

func (concatenate *Concatenate) GetPrecedence() int {
	return 1
}

func (concatenate *Concatenate) ToNFADesign() *NFADesign {
	firstNFADesign := concatenate.first.ToNFADesign()
	secondNFADesign := concatenate.second.ToNFADesign()

	startState := firstNFADesign.startState
	acceptStates := secondNFADesign.acceptStates
	rules := make([]*FARule, 0, len(firstNFADesign.rulebook.rules)+len(secondNFADesign.rulebook.rules)+len(firstNFADesign.acceptStates))
	rules = append(rules, firstNFADesign.rulebook.rules...)
	rules = append(rules, secondNFADesign.rulebook.rules...)
	for _, state := range firstNFADesign.acceptStates {
		rule := NewFARule(state, 0, secondNFADesign.startState)
		rules = append(rules, rule)
	}
	rulebook := NewNFARulebook(rules)

	return NewNFADesign(startState, acceptStates, rulebook)
}

type Choose struct {
	first, second Pattern
}

func NewChoose(first, second Pattern) *Choose {
	return &Choose{
		first:  first,
		second: second,
	}
}

func (choose *Choose) String() string {
	return bracket(choose.first, choose.GetPrecedence()) + "|" + bracket(choose.second, choose.GetPrecedence())
}

func (choose *Choose) GetPrecedence() int {
	return 0
}

func (choose *Choose) ToNFADesign() *NFADesign {
	firstNFADesign := choose.first.ToNFADesign()
	secondNFADesign := choose.second.ToNFADesign()

	v0 := 0
	p0 := &v0
	startState := p0
	acceptStates := make([]State, 0, len(firstNFADesign.acceptStates)+len(secondNFADesign.acceptStates))
	acceptStates = append(acceptStates, firstNFADesign.acceptStates...)
	acceptStates = append(acceptStates, secondNFADesign.acceptStates...)

	rules := make([]*FARule, 0, len(firstNFADesign.rulebook.rules)+len(secondNFADesign.rulebook.rules)+2)
	rules = append(rules, firstNFADesign.rulebook.rules...)
	rules = append(rules, secondNFADesign.rulebook.rules...)
	rules = append(rules, NewFARule(startState, 0, firstNFADesign.startState))
	rules = append(rules, NewFARule(startState, 0, secondNFADesign.startState))
	rulebook := NewNFARulebook(rules)

	return NewNFADesign(startState, acceptStates, rulebook)
}

type Repeat struct {
	pattern Pattern
}

func NewRepeat(pattern Pattern) *Repeat {
	return &Repeat{
		pattern: pattern,
	}
}

func (repeat *Repeat) String() string {
	return bracket(repeat.pattern, repeat.GetPrecedence())
}

func (repeat *Repeat) GetPrecedence() int {
	return 2
}

func (repeat *Repeat) ToNFADesign() *NFADesign {
	patternNFADesign := repeat.pattern.ToNFADesign()

	v0 := 0
	p0 := &v0
	startState := p0
	acceptStates := make([]State, 0, len(patternNFADesign.acceptStates)+1)
	acceptStates = append(acceptStates, patternNFADesign.acceptStates...)
	acceptStates = append(acceptStates, startState)

	rules := make([]*FARule, 0, len(patternNFADesign.rulebook.rules)+len(patternNFADesign.acceptStates)+1)
	rules = append(rules, patternNFADesign.rulebook.rules...)
	for _, acceptState := range patternNFADesign.acceptStates {
		rule := NewFARule(acceptState, 0, patternNFADesign.startState)
		rules = append(rules, rule)
	}
	rules = append(rules, NewFARule(startState, 0, patternNFADesign.startState))
	rulebook := NewNFARulebook(rules)

	return NewNFADesign(startState, acceptStates, rulebook)
}
