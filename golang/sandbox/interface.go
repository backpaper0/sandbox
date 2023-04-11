package main

type Greeting interface {
	say() string
}

type HelloEn struct {
}

func (*HelloEn) say() string {
	return "Hello"
}

type HelloJa struct {
}

func (*HelloJa) say() string {
	return "こんにちは"
}
