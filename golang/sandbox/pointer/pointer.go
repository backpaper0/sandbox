package pointer

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

func updateParameter(s1 *string) {
	*s1 = "updated"
	s2 := "foobar"
	// ポインター型の変数に新しいポインターを代入しただけなので元のポインターには何も影響しない
	s1 = &s2
}

type Example5Arg struct {
	Value1 string
	Value2 *string
}

type Example5ReturnValue struct {
	Value1, Value2, Value3, Value4, Value5, Value6 string
}

func handleExample5(arg1 Example5Arg, arg2 *Example5Arg) Example5ReturnValue {
	return Example5ReturnValue{
		arg1.Value1,
		*arg1.Value2,
		*(arg1.Value2),
		arg2.Value1,
		*arg2.Value2,
		*(*arg2).Value2,
	}
}
