package pipe

import (
	"fmt"
	"io"
	"os"
	"testing"
)

func TestPipe(t *testing.T) {
	r, w, _ := os.Pipe()

	msg := "Hello world"
	fmt.Fprint(w, msg)
	w.Close()

	b, _ := io.ReadAll(r)
	if string(b) != msg {
		t.Fail()
	}
}
