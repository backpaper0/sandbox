package snippet

import (
	"fmt"
	"reflect"
	"testing"
)

func TestReverseArray(t *testing.T) {
	fixtures := []struct {
		input    []int
		expected []int
	}{
		{[]int{}, []int{}},
		{[]int{1}, []int{1}},
		{[]int{1, 2, 3, 4, 5, 6, 7, 8, 9}, []int{9, 8, 7, 6, 5, 4, 3, 2, 1}},
		{[]int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, []int{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}},
	}
	for i, fixture := range fixtures {
		t.Run(fmt.Sprintf("case_%v", i), func(t *testing.T) {
			ReverseArray(fixture.input)
			if !reflect.DeepEqual(fixture.input, fixture.expected) {
				t.Errorf("Expected is %v but actual is %v", fixture.expected, fixture.input)
			}
		})
	}
}
