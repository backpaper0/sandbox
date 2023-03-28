package main

import (
	"encoding/json"
	"fmt"
)

type Root struct {
	Aaa string
	Bbb float64
	Ccc bool
	// interface{} は雑に言うと java.lang.Object みたいなもの
	Eee []interface{}
	Fff Child
}

type Child struct {
	Ggg string
}

func main() {
	s := `
		{
			"aaa": "hello",
			"bbb": 123,
			"ccc": true,
			"ddd": null,
			"eee": ["hello2", 456, false],
			"fff": {
				"ggg": "hello3"
			}
		}
	`

	var r Root

	err := json.Unmarshal([]byte(s), &r)
	if err != nil {
		panic(err)
	}

	fmt.Println(r)

	for _, eee := range r.Eee {
		switch eee.(type) {
		case string:
			fmt.Println("string: ", eee)
		case float64:
			fmt.Println("float64: ", eee)
		default:
			fmt.Printf("(%T): %v\n", eee, eee)
		}
	}
}
