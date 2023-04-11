package main

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

	b1 := strings.Builder{}
	b2 := strings.Builder{}
	b3 := strings.Builder{}

	fmt.Fprintf(&b1, "%p", p1)
	fmt.Fprintf(&b2, "%p", p2)
	fmt.Fprintf(&b3, "%p", p3)

	// p1, p2, p3それぞれアドレスが変わっていることが確認できる。
	// つまり引数を渡すときや戻り値を返すときに値がコピーされていることがわかる。
	if b1.String() == b2.String() {
		t.Fail()
	}
	if b1.String() == b3.String() {
		t.Fail()
	}
	if b2.String() == b3.String() {
		t.Fail()
	}
}

func TestStructureMember(t *testing.T) {
	var p4, p5, p6 *string
	pe1 := PointerExample2{s: "Hello"}
	p4 = &pe1.s
	pe2, p5 := pointerExample3(pe1)
	p6 = &pe2.s

	b1 := strings.Builder{}
	b2 := strings.Builder{}
	b3 := strings.Builder{}

	fmt.Fprintf(&b1, "%p", p4)
	fmt.Fprintf(&b2, "%p", p5)
	fmt.Fprintf(&b3, "%p", p6)

	// やはりアドレスが変わっていることが確認できる。
	// 構造体のメンバーも値がコピーされていることがわかる。
	if b1.String() == b2.String() {
		t.Fail()
	}
	if b1.String() == b3.String() {
		t.Fail()
	}
	if b2.String() == b3.String() {
		t.Fail()
	}
}

func TestMethod(t *testing.T) {
	pe1 := PointerExample2{s: "Hello"}
	pe3 := PointerExample2{s: "Hello"}
	p7 := &pe3
	pe4, p8 := pe1.pointerExample4()
	p9 := &pe4

	b1 := strings.Builder{}
	b2 := strings.Builder{}
	b3 := strings.Builder{}

	fmt.Fprintf(&b1, "%p", p7)
	fmt.Fprintf(&b2, "%p", p8)
	fmt.Fprintf(&b3, "%p", p9)

	if b1.String() == b2.String() {
		t.Fail()
	}
	if b1.String() == b3.String() {
		t.Fail()
	}
	if b2.String() == b3.String() {
		t.Fail()
	}
}
