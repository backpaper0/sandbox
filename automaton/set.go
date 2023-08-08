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

func (set *Set) Contains(value State) bool {
	_, ok := set.values[value]
	return ok
}

func (set *Set) ContainsSet(other *Set) bool {
	for _, k := range other.Values() {
		if !set.Contains(k) {
			return false
		}
	}
	return true
}

func (set *Set) Values() []State {
	ks := make([]State, 0, len(set.values))
	for k := range set.values {
		ks = append(ks, k)
	}
	return ks
}

func (set *Set) AddAll(addMe *Set) {
	set.Add(addMe.Values()...)
}

func (set *Set) Equals(other *Set) bool {
	getKeys := func(s *Set) []State {
		ks := s.Values()
		sort.Slice(ks, func(i, j int) bool {
			return *ks[i] < *ks[j]
		})
		return ks
	}

	keys1 := getKeys(set)
	keys2 := getKeys(other)

	return reflect.DeepEqual(keys1, keys2)
}

func (set *Set) Size() int {
	return len(set.values)
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
