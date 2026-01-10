package main

/*
#include <stdio.h>
#include <stdint.h>

typedef void (*callback_func)(int32_t);

static void invoke_callback(callback_func cb, int32_t value) {
    if (cb != NULL) {
        cb(value);
    }
}
*/
import "C"
import "fmt"

//export addWithCallback
func addWithCallback(a C.int32_t, b C.int32_t, callback C.callback_func) C.int32_t {
	result := a + b
	fmt.Printf("[Go] %d + %d = %d\n", a, b, result)
	C.invoke_callback(callback, result)
	return result
}

func main() {}
