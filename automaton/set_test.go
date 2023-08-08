package main

import "testing"

func TestSet(t *testing.T) {
	v1, v2, v3 := 1, 2, 3
	s1, s2, s3 := &v1, &v2, &v3
	set := NewSet()
	set.Add(s1)
	set.Add(s2, s3)

	if expected := NewSet(s3, s2, s1); !set.Equals(expected) {
		t.Errorf("Expected is %v but actual is %v", set, expected)
	}
}
