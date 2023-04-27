package main

import (
	"embed"
	"fmt"
)

//go:embed hello.txt
var hello string

//go:embed static/*.txt
var static embed.FS

func main() {
	fmt.Println(hello)

	names := []string{"foo", "bar"}
	for _, name := range names {
		bs, err := static.ReadFile("static/" + name + ".txt")
		if err != nil {
			panic(err)
		}
		fmt.Println(string(bs))
	}
}
