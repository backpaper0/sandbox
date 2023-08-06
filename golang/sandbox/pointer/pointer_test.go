package pointer

import (
	"fmt"
	"strings"
	"testing"
)

func TestPointer(t *testing.T) {
	s1 := "Hello Pointer"
	p1 := &s1
	s2, p2 := pointerExample1(s1)
	p3 := &s2

	b1 := fmt.Sprintf("%p", p1)
	b2 := fmt.Sprintf("%p", p2)
	b3 := fmt.Sprintf("%p", p3)

	// p1, p2, p3それぞれアドレスが変わっていることが確認できる。
	// つまり引数を渡すときや戻り値を返すときに値がコピーされていることがわかる。
	if b1 == b2 {
		t.Fail()
	}
	if b1 == b3 {
		t.Fail()
	}
	if b2 == b3 {
		t.Fail()
	}
}

func TestPointerStructMember(t *testing.T) {
	var p4, p5, p6 *string
	pe1 := PointerExample2{s: "Hello"}
	p4 = &pe1.s
	pe2, p5 := pointerExample3(pe1)
	p6 = &pe2.s

	b1 := fmt.Sprintf("%p", p4)
	b2 := fmt.Sprintf("%p", p5)
	b3 := fmt.Sprintf("%p", p6)

	// やはりアドレスが変わっていることが確認できる。
	// 構造体のメンバーも値がコピーされていることがわかる。
	if b1 == b2 {
		t.Fail()
	}
	if b1 == b3 {
		t.Fail()
	}
	if b2 == b3 {
		t.Fail()
	}
}

func TestPointerMethod(t *testing.T) {
	pe1 := PointerExample2{s: "Hello"}
	pe3 := PointerExample2{s: "Hello"}
	p7 := &pe3
	pe4, p8 := pe1.pointerExample4()
	p9 := &pe4

	b1 := fmt.Sprintf("%p", p7)
	b2 := fmt.Sprintf("%p", p8)
	b3 := fmt.Sprintf("%p", p9)

	if b1 == b2 {
		t.Fail()
	}
	if b1 == b3 {
		t.Fail()
	}
	if b2 == b3 {
		t.Fail()
	}
}

func TestPointerSliceArg(t *testing.T) {
	f := func(arr []string) string {
		return fmt.Sprintf("%p", arr)
	}
	arr := []string{"foo", "bar", "baz"}
	if f(arr) != fmt.Sprintf("%p", arr) {
		t.Fail()
	}
}

func TestPointerSliceReturnValue(t *testing.T) {
	f := func() (arr []string, s string) {
		arr = []string{"foo", "bar", "baz"}
		s = fmt.Sprintf("%p", arr)
		return
	}
	arr, s := f()
	if s != fmt.Sprintf("%p", arr) {
		t.Fail()
	}
}

func TestPointerMapArg(t *testing.T) {
	f := func(m map[string]string) string {
		return fmt.Sprintf("%p", m)
	}
	m := map[string]string{"foo": "hello", "bar": "123", "baz": "true"}
	if f(m) != fmt.Sprintf("%p", m) {
		t.Fail()
	}
}

func TestPointerMapReturnValue(t *testing.T) {
	f := func() (m map[string]string, s string) {
		m = map[string]string{"foo": "hello", "bar": "123", "baz": "true"}
		s = fmt.Sprintf("%p", m)
		return
	}
	m, s := f()
	if s != fmt.Sprintf("%p", m) {
		t.Fail()
	}
}

func TestPointerUpdateParameter(t *testing.T) {
	s := "init"
	updateParameter(&s)
	expected := "updated"
	if s != expected {
		t.Errorf("Expected is %v but actual is %v", expected, s)
	}
}

func TestPointerOperator(t *testing.T) {
	s := "hello"
	arg := Example5Arg{s, &s}
	actual := handleExample5(arg, &arg)
	expected := Example5ReturnValue{"hello", "hello", "hello", "hello", "hello", "hello"}
	if actual != expected {
		t.Errorf("Expected is %v but actual is %v", expected, actual)
	}
}

func TestPointerAsign(t *testing.T) {
	// 変数の代入でも値がコピーされる
	a := Example6{"foo"}
	b := a
	c := &a
	t.Logf("&a: %p\n", &a)
	t.Logf("&b: %p\n", &b)
	t.Logf(" c: %p\n", c)

	(&b).Value = "bar"
	if a.Value == "bar" {
		t.Errorf("a.Value is bar: a = %v", a)
	}

	c.Value = "baz"
	if a.Value != "baz" {
		t.Errorf("a.Value is not baz: a = %v", a)
	}
}

func TestPointerSliceElement(t *testing.T) {
	a := []int{1, 2, 3}

	b := a[1] // d == 2
	a[1] = 20
	c := a[1] // c == 20
	if b == c {
		t.Errorf("%v", b)
	}

	d := &a[2] // *d == 3
	a[2] = 30
	e := &a[2] // *e == 30, *d == 30
	if *d != *e || *d != 30 || *e != 30 {
		t.Errorf("d = %p %v\ne = %p %v\n", d, *d, e, *e)
	}
}

func TestPointerEqual(t *testing.T) {
	a := 0
	b := 0

	p1 := &a
	p2 := &a
	p3 := &b

	if p1 != p2 {
		t.Error()
		return
	}
	if p1 == p3 {
		t.Error()
		return
	}
}

func TestPointerString(t *testing.T) {
	a := 0
	p := &a
	s := fmt.Sprintf("%v", p) // 0xc00001a2d8 のような形式
	if !strings.HasPrefix(s, "0x") {
		t.Error(s)
	}
}
