package algorithm

import (
	"reflect"
	"testing"
)

func TestDepthFirstSearch(t *testing.T) {
	root := &Tree{
		Left: &Tree{
			Left: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
			Right: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
		},
		Right: &Tree{
			Left: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
			Right: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
		},
	}

	DepthFirstSearch(root)

	expected := &Tree{
		Value: 1,
		Left: &Tree{
			Value: 2,
			Left: &Tree{
				Value: 3,
				Left:  &Tree{Value: 4},
				Right: &Tree{Value: 5},
			},
			Right: &Tree{
				Value: 6,
				Left:  &Tree{Value: 7},
				Right: &Tree{Value: 8},
			},
		},
		Right: &Tree{
			Value: 9,
			Left: &Tree{
				Value: 10,
				Left:  &Tree{Value: 11},
				Right: &Tree{Value: 12},
			},
			Right: &Tree{
				Value: 13,
				Left:  &Tree{Value: 14},
				Right: &Tree{Value: 15},
			},
		},
	}

	if !reflect.DeepEqual(root, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, root)
	}
}

func TestBreadthFirstSearch(t *testing.T) {
	root := &Tree{
		Left: &Tree{
			Left: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
			Right: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
		},
		Right: &Tree{
			Left: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
			Right: &Tree{
				Left:  &Tree{},
				Right: &Tree{},
			},
		},
	}

	BreadthFirstSearch(root)

	expected := &Tree{
		Value: 1,
		Left: &Tree{
			Value: 2,
			Left: &Tree{
				Value: 4,
				Left:  &Tree{Value: 8},
				Right: &Tree{Value: 9},
			},
			Right: &Tree{
				Value: 5,
				Left:  &Tree{Value: 10},
				Right: &Tree{Value: 11},
			},
		},
		Right: &Tree{
			Value: 3,
			Left: &Tree{
				Value: 6,
				Left:  &Tree{Value: 12},
				Right: &Tree{Value: 13},
			},
			Right: &Tree{
				Value: 7,
				Left:  &Tree{Value: 14},
				Right: &Tree{Value: 15},
			},
		},
	}

	if !reflect.DeepEqual(root, expected) {
		t.Errorf("Expected is %v but actual is %v", expected, root)
	}
}
