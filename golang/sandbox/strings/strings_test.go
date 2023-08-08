package strings

import (
	"io"
	"strings"
	"testing"
)

func TestNewReader(t *testing.T) {
	r := strings.NewReader("foo")
	if ch, size, err := r.ReadRune(); ch != 'f' || size != 1 || err != nil {
		t.Error()
		return
	}
	if ch, size, err := r.ReadRune(); ch != 'o' || size != 1 || err != nil {
		t.Error()
		return
	}
	if ch, size, err := r.ReadRune(); ch != 'o' || size != 1 || err != nil {
		t.Error()
		return
	}
	if ch, size, err := r.ReadRune(); ch != 0 || size != 0 || err != io.EOF {
		t.Error()
		return
	}
}
