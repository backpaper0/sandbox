package main

import (
	"fmt"
	"reflect"
	"sort"
)

type Set struct {
	values map[State]struct{}
}

func NewSet(values ...State) *Set {
	set := &Set{
		values: make(map[State]struct{}),
	}
	set.Add(values...)
	return set
}

func (set *Set) Add(values ...State) {
	for _, v := range values {
		set.values[v] = struct{}{}
	}
}

func (set *Set) Equals(other *Set) bool {
	getKeys := func(s *Set) []State {
		ks := make([]State, 0, len(s.values))
		for k := range s.values {
			ks = append(ks, k)
		}
		sort.Slice(ks, func(i, j int) bool {
			return *ks[i] < *ks[j]
		})
		return ks
	}

	keys1 := getKeys(set)
	keys2 := getKeys(other)

	return reflect.DeepEqual(keys1, keys2)
}

func (set *Set) String() string {
	keys := make([]int, 0, len(set.values))
	for k := range set.values {
		keys = append(keys, *k)
	}
	sort.Slice(keys, func(i, j int) bool {
		return keys[i] < keys[j]
	})
	return fmt.Sprintf("%v", keys)
}
