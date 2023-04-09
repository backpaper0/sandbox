package main

import "fmt"

func main() {
	// 変数宣言

	// 条件分岐

	// ループ

	// interface
	fmt.Println("**** interface ****")
	var hello Greeting
	hello = HelloEn{}
	fmt.Println(hello.say())
	hello = HelloJa{}
	fmt.Println(hello.say())
}

type Greeting interface {
	say() string
}

type HelloEn struct {
}

func (self HelloEn) say() string {
	return "Hello"
}

type HelloJa struct {
}

func (self HelloJa) say() string {
	return "こんにちは"
}
