package main

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
