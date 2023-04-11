package main

import (
	"testing"
)

func TestInterface(t *testing.T) {
	var hello Greeting

	hello = &HelloEn{}
	if hello.say() != "Hello" {
		t.Fail()
	}

	hello = &HelloJa{}
	if hello.say() != "こんにちは" {
		t.Fail()
	}
}
