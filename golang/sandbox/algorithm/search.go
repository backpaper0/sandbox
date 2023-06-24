package algorithm

import (
	"container/list"
	"fmt"
)

func DepthFirstSearch(root *Tree) {
	value := 0
	var walk func(*Tree)
	walk = func(tree *Tree) {
		if tree == nil {
			return
		}
		value += 1
		tree.Value = value
		walk(tree.Left)
		walk(tree.Right)
	}
	walk(root)
}

func BreadthFirstSearch(root *Tree) {
	value := 0
	queue := list.New()
	queue.PushBack(root)
	for queue.Len() > 0 {
		tree := queue.Remove(queue.Front()).(*Tree)
		if tree != nil {
			value += 1
			tree.Value = value
			queue.PushBack(tree.Left)
			queue.PushBack(tree.Right)
		}
	}
}

type Tree struct {
	Left, Right *Tree
	Value       int
}

func (tree *Tree) String() string {
	if tree == nil {
		return ""
	}
	return fmt.Sprintf("%v (%v, %v)", tree.Value, tree.Left, tree.Right)
}
