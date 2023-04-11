package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"strings"
)

func main() {

	records := [][]string{
		{"1", "foo"},
		{"2", "b,a,r"},
		{"3", `b"a"z`},
		{"4", `q
u
x`},
	}

	sb := strings.Builder{}

	w := csv.NewWriter(&sb)
	for _, record := range records {
		err := w.Write(record)
		if err != nil {
			panic(err)
		}
	}
	w.Flush()

	s := sb.String()

	sr := strings.NewReader(s)
	r := csv.NewReader(sr)

	fmt.Println("**** Write CSV ****")
	fmt.Println(s)

	fmt.Println("**** Read CSV ****")
	for {
		line, err := r.Read()
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		fmt.Println(line)
	}
}
