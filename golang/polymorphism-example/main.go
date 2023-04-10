package main

import (
	"flag"
	"fmt"
)

func main() {
	lang := flag.String("lang", "ja", "jaまたはenを指定してください")
	flag.Parse()
	hello := buildHello(*lang)
	msg := hello.Say()
	fmt.Println(msg)
}

type Hello interface {
	Say() string
}

func buildHello(lang string) Hello {
	if lang == "en" {
		return HelloEn{}
	}
	return HelloJa{}
}

type HelloJa struct {
}

type HelloEn struct {
}

func (hello HelloJa) Say() string {
	return "こんにちは"
}

func (hello HelloEn) Say() string {
	return "Hello"
}
