package main

import (
	"flag"
	"fmt"
)

func main() {
	var foo = flag.String("foo", "default", "usage")
	var bar = flag.Int("bar", 123, "usage")
	var baz = flag.Bool("baz", false, "usage")
	flag.Parse()
	fmt.Println(*foo, *bar, *baz)
}
