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
