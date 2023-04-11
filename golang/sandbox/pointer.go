package main

func pointerExample1(s string) (string, *string) {
	return s, &s
}

type PointerExample2 struct {
	s string
}

func pointerExample3(pe PointerExample2) (PointerExample2, *string) {
	return pe, &pe.s
}

func (pe PointerExample2) pointerExample4() (PointerExample2, *PointerExample2) {
	return pe, &pe
}
