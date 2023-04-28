package main

import (
	"flag"
	"fmt"
	"os"
)

var err *bool

func init() {
	err = flag.Bool("err", false, "...")
}

func main() {
	flag.Parse()
	if *err {
		fmt.Fprint(os.Stderr, "Error!!!")
		os.Exit(1)
	}
	fmt.Print("Hello World")
}
