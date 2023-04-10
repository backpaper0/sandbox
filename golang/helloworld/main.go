package main

import "fmt"

func main() {
	// 変数宣言

	// 条件分岐

	// ループ

	// interface
	fmt.Println("**** interface ****")
	var hello Greeting
	hello = &HelloEn{}
	fmt.Println(hello.say())
	hello = &HelloJa{}
	fmt.Println(hello.say())

	// ポインター
	fmt.Println("**** ポインター ****")
	pointerExample()
}

//**** interface ****

type Greeting interface {
	say() string
}

type HelloEn struct {
}

func (*HelloEn) say() string {
	return "Hello"
}

type HelloJa struct {
}

func (*HelloJa) say() string {
	return "こんにちは"
}

// **** ポインター ****

func pointerExample() {

	fmt.Print("引数や戻り値のアドレスを確認")
	s1 := "Hello Pointer"
	p1 := &s1
	s2, p2 := pointerExample1(s1)
	p3 := &s2
	// p1, p2, p3それぞれアドレスが変わっていることが確認できる。
	// つまり引数を渡すときや戻り値を返すときに値がコピーされていることがわかる。
	fmt.Printf("p1 = %p\n", p1)
	fmt.Printf("p2 = %p\n", p2)
	fmt.Printf("p3 = %p\n", p3)

	fmt.Println("構造体のフィールドで試す")
	var p4, p5, p6 *string
	pe1 := PointerExample2{s: "Hello"}
	p4 = &pe1.s
	pe2, p5 := pointerExample3(pe1)
	p6 = &pe2.s
	// やはりアドレスが変わっていることが確認できる。
	// 構造体のメンバーも値がコピーされていることがわかる。
	fmt.Printf("p4 = %p\n", p4)
	fmt.Printf("p5 = %p\n", p5)
	fmt.Printf("p6 = %p\n", p6)

	fmt.Println("メソッドで試す")
	pe3 := PointerExample2{s: "Hello"}
	p7 := &pe3
	pe4, p8 := pe1.pointerExample4()
	p9 := &pe4
	fmt.Printf("p7 = %p\n", p7)
	fmt.Printf("p8 = %p\n", p8)
	fmt.Printf("p9 = %p\n", p9)
}

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
