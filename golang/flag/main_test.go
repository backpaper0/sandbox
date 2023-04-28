package main

import (
	"flag"
	"testing"
)

func TestFoo(t *testing.T) {
	flag.CommandLine.Set("foo", "hello")
	main()
	if *foo != "hello" {
		t.Fail()
	}
}

func TestBar(t *testing.T) {
	flag.CommandLine.Set("bar", "456")
	main()
	if *bar != 456 {
		t.Fail()
	}
}

func TestBaz(t *testing.T) {
	flag.CommandLine.Set("baz", "true")
	main()
	if *baz != true {
		t.Fail()
	}
}

func ExampleMain() {
	flag.CommandLine.Set("foo", "abc")
	flag.CommandLine.Set("bar", "789")
	flag.CommandLine.Set("baz", "false")
	main()
	// Output:
	// abc 789 false
}
