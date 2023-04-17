package main

import (
	"encoding/csv"
	"os"

	"golang.org/x/text/encoding/japanese"
	"golang.org/x/text/transform"
)

func main() {
	file, err := os.Create("output")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	w := csv.NewWriter(transform.NewWriter(file, japanese.ShiftJIS.NewEncoder()))
	w.Write([]string{"こんにちは", "世界"})
	w.Flush()
}
