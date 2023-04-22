package init

import (
	"reflect"
	"testing"
)

func TestInit(t *testing.T) {
	actual := example()
	expected := []int{1, 1, 2, 2, 3, 3, 4, 0}
	if !reflect.DeepEqual(actual, expected) {
		t.Errorf("Expected is %v but actual is %v\n", expected, actual)
	}
}
