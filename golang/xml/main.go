package main

import (
	"encoding/xml"
	"fmt"
)

type Root struct {
	Child string `xml:"child"`
	Child23 string `xml:"child2>child3"`
}

func main() {
	s := `
		<root>
			<child>content</child>
			<child2>
				<child3>content2</child3>
			</child2>
		</root>
	`

	r := Root{Child: "", Child23:""}

	err := xml.Unmarshal([]byte(s), &r)
	if err != nil {
		panic(err)
	}

	fmt.Println(r.Child)
	fmt.Println(r.Child23)
}
