package main

import (
	"flag"
	"fmt"

	"github.com/backpaper0/sandbox/golang/parser-example/parser"
)

func main() {

	expr := flag.String("e", "", "Expression")
	flag.Parse()

	prsr, err := parser.NewParser(*expr)
	if err != nil {
		panic(err)
	}
	node, err := prsr.Parse()
	if err != nil {
		panic(err)
	}
	result := parser.Evaluate(node)

	fmt.Printf("%v = %v\n", *expr, result)
}
