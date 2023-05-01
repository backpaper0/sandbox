package main

import (
	"fmt"
	"runtime"
)

func main() {
	fmt.Printf("GOOS=%v\n", runtime.GOOS)
	fmt.Printf("GOARCH=%v\n", runtime.GOARCH)
}

