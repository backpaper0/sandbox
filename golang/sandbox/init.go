package main

// リテラルによる初期化、メソッドによる初期化、initメソッドの呼び出しの順に処理されているっぽい

var a1 = 1
var a2 int

var b1 = two()
var b2 int

var c1 = 3
var c2 int

var d1, d2 int

func init() {
	b2 = b1
	d1 = 4
}

func two() int {
	a2 = a1
	c2 = c1
	d2 = d1 // initメソッドよりも先に処理されるためd2には0が代入される
	return 2
}

func example() []int {
	return []int{a1, a2, b1, b2, c1, c2, d1, d2}
}
