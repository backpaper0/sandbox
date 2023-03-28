package main

import (
	"encoding/xml"
	"fmt"
)

type Root struct {
	Child   string `xml:"child"`
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

	// r := Root{Child: "", Child23:""}
	var r Root     // わざわざ上記のようにしなくても変数を宣言すればそれで良かった
	fmt.Println(r) // どんな状態か見てみたいので出力もしておく

	err := xml.Unmarshal([]byte(s), &r)
	if err != nil {
		panic(err)
	}

	fmt.Println(r.Child)
	fmt.Println(r.Child23)

	fmt.Println(r) // 最後に全部出力しておく
}
