package main

import (
	"encoding/json"
	"reflect"
	"strings"
	"testing"
)

func TestJson(t *testing.T) {

	r := strings.NewReader(`
	{
		"foo": "hello",
		"bar": 123,
		"baz": true,
		"qux": ["x", "y"],
		"hoge_fuga_piyo": "z"
	}
	`)
	d := json.NewDecoder(r)
	v := Foobar{}
	d.Decode(&v)

	expected := Foobar{"hello", 123, true, []string{"x", "y"}, "z"}

	if !reflect.DeepEqual(v, expected) {
		t.Errorf("Expected %v but actual %v", expected, v)
	}
}

func TestJsonMap(t *testing.T) {

	r := strings.NewReader(`
	{
		"foo": "hello",
		"bar": 123,
		"baz": true,
		"qux": ["x", "y"]
	}
	`)
	d := json.NewDecoder(r)
	v := make(map[string]interface{})
	d.Decode(&v)

	expected := map[string]interface{}{
		"foo": "hello",
		"bar": float64(123),
		"baz": true,
		"qux": []interface{}{"x", "y"},
	}

	if !reflect.DeepEqual(v, expected) {
		t.Errorf("%v\n%v\n", v, expected)
	}
}

type Foobar struct {
	Foo          string
	Bar          int
	Baz          bool
	Qux          []string
	HogeFugaPiyo string `json:"hoge_fuga_piyo"`
}
