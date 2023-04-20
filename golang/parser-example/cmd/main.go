package main

import (
	"flag"
	"fmt"
	"os"

	"github.com/backpaper0/sandbox/golang/parser-example/parser"
)

func main() {

	expr := flag.String("e", "", "Expression")
	flag.Parse()

	if *expr == "" {
		fmt.Fprintln(os.Stderr, "-eオプションで数式を指定してください")
		os.Exit(1)
	}

	prsr, err := parser.NewParser(*expr)
	if err != nil {
		fmt.Fprintln(os.Stderr, err.Error())
		os.Exit(1)
	}
	node, err := prsr.Parse()
	if err != nil {
		fmt.Fprintln(os.Stderr, err.Error())
		os.Exit(1)
	}
	result, err := parser.Evaluate(node)
	if err != nil {
		fmt.Fprintln(os.Stderr, err.Error())
		os.Exit(1)
	}

	fmt.Printf("%v = %v\n", *expr, result)
}
