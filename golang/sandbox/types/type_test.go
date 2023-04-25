package types

import (
	"testing"
)

func TestTypeStruct1(t *testing.T) {
	var a TypeExample1
	if a.i != 0 {
		t.Fail()
	}
	if a.s != "" {
		t.Fail()
	}
}

func TestTypeStruct2(t *testing.T) {
	a := TypeExample1{}
	if a.i != 0 {
		t.Fail()
	}
	if a.s != "" {
		t.Fail()
	}
}

func TestTypeInterface1(t *testing.T) {
	var a TypeExample2
	if a != nil {
		t.Fail()
	}
}

func TestTypeFunction(t *testing.T) {
	a := func(name string) string {
		return "hello " + name
	}
	if a("world") != "hello world" {
		t.Fail()
	}
}

func TestTypeMap(t *testing.T) {
	a := make(map[string]string)
	a["foo"] = "bar"
	if a["foo"] != "bar" {
		t.Fail()
	}
}

func TestDefinedType(t *testing.T) {
	var a DefinedTypeExample = 1
	var b int = 1
	c := DefinedTypeExample(b)
	if a != c {
		t.Fail()
	}

	// if a != b { // 型が異なるのでコンパイルエラー
	// 	t.Fail()
	// }
}

func TestTypeAlias(t *testing.T) {
	var a TypeAliasExample = 1
	var b int = 1
	if a != b {
		t.Fail()
	}
}

func TestDefinedType2(t *testing.T) {
	a := DefinedTypeExample(1)
	b := DefinedTypeExample2(1)
	c := DefinedTypeExample(b) // 異なるdefined typeへ変換する
	if a != c {
		t.Fail()
	}
}
