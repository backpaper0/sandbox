package main

import (
	"testing"
)

func TestHello(t *testing.T) {
	// c.f. https://go.dev/doc/code#Testing
	cases := []struct {
		in, want string
	}{
		{"world", "Hello, world!"},
		{"foo", "Hello, foo!"},
		{"bar", "Hello, bar!"},
	}
	for _, c := range cases {
		got := Hello(c.in)
		if got != c.want {
			t.Fail()
		}
	}
}
