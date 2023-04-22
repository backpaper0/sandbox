package tostring

import (
	"fmt"
	"testing"
)

func TestToString(t *testing.T) {
	// fmt.Spring などでは String() string があれば呼び出される
	// 参考 https://pkg.go.dev/fmt#hdr-Printing
	a := ToStringExample{"foo"}
	b := fmt.Sprint(a)
	c := "foofoo"
	if b != c {
		t.Errorf("Expected is %[2]v but actual is %[1]v", b, c)
	}
}

type ToStringExample struct {
	Value string
}

func (a ToStringExample) String() string {
	return a.Value + a.Value
}
