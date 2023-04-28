package main

import (
	"flag"
	"fmt"
)

var foo *string
var bar *int
var baz *bool

func init() {
	foo = flag.String("foo", "default", "usage")
	bar = flag.Int("bar", 123, "usage")
	baz = flag.Bool("baz", false, "usage")
}
func main() {
	flag.Parse()
	fmt.Println(*foo, *bar, *baz)
}
