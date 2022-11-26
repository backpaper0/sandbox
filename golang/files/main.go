package main

import (
	"fmt"
	"io/fs"
	"os"
	"strings"
)

func main() {
	root := os.DirFS("./")
	fs.WalkDir(root, ".", func(path string, d fs.DirEntry, err error) error {
		if err != nil {
			panic(err)
		}
		if !d.IsDir() && strings.HasSuffix(path, "/demo.txt") {
			fmt.Println(path)
		}
		return nil
	})
}
