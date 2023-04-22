package receiver

import "testing"

func TestReceiver(t *testing.T) {
	// var myInt MyInt = 123
	myInt := MyInt(123)
	if myInt.self() != 123 {
		t.Fail()
	}
}

type MyInt int

func (myInt MyInt) self() MyInt {
	return myInt
}

// これは cannot define new methods on non-local type int と怒られてコンパイルが通らない
// func (i int) self() int {
// 	return i
// }

func TestReceiverPointerOrValue(t *testing.T) {
	// メソッドのレシーバーはポインターかどうかを気にしない
	a := MyString("hello")
	if a.method1() != "hello" {
		t.Fail()
	}
	if (&a).method1() != "hello" {
		t.Fail()
	}
	if a.method2() != "hello" {
		t.Fail()
	}
	if (&a).method2() != "hello" {
		t.Fail()
	}
}

type MyString string

func (s MyString) method1() MyString {
	return s
}

func (s *MyString) method2() MyString {
	return *s
}

func TestReceiverNil(t *testing.T) {
	// nilでもメソッドを呼び出せる
	var a *MyInt
	if !a.isNil() {
		t.Fail()
	}
}

func (a *MyInt) isNil() bool {
	return a == nil
}
