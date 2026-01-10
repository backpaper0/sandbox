package main

/*
#include <stdio.h>
*/
import "C"

//export add
func add(a C.int32_t, b C.int32_t) C.int32_t {
	return a + b
}

func main() {}
