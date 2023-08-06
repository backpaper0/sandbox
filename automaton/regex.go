package main

type Pattern interface {
	String() string
	GetPrecedence() int
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
