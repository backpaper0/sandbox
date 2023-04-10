package main

import (
	"flag"
	"fmt"
)

func main() {
	lang := flag.String("lang", "ja", "jaまたはenを指定してください")
	flag.Parse()

	// langをもとにして「HelloJa構造体のポインター」または「HelloEn構造体のポインター」のどちらかを取得している。
	// こういったことができるのはbuildHelloメソッドの戻り値はHelloインターフェースであり、
	// HelloJa構造体のポインターとHelloEn構造体のポインターはどちらもHelloインターフェースを満たしているから。
	// ポインターにしているのは値のコピーを減らすため。
	hello := buildHello(*lang)

	msg := hello.Say()
	fmt.Println(msg)
}

type Hello interface {
	Say() string
}

func buildHello(lang string) Hello {
	if lang == "en" {
		return &HelloEn{}
	}
	return &HelloJa{}
}

type HelloJa struct {
}

type HelloEn struct {
}

func (hello *HelloJa) Say() string {
	return "こんにちは"
}

func (hello *HelloEn) Say() string {
	return "Hello"
}
