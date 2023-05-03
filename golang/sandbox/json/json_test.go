package json

import (
	"encoding/json"
	"reflect"
	"testing"
)

func TestJsonToMap(t *testing.T) {
	data := []byte(`{
		"foo": "hello",
		"bar": 123,
		"baz": true,
		"qux": ["world", 456]
	}`)
	v := map[string]interface{}{}
	json.Unmarshal(data, &v)

	t.Logf("%v", v)

	foo, ok := v["foo"].(string)
	if !ok || foo != "hello" {
		name := "foo"
		t.Errorf("%v is %v %v", name, v[name], reflect.TypeOf(v[name]))
	}
	bar, ok := v["bar"].(float64)
	if !ok || bar != 123 {
		name := "bar"
		t.Errorf("%v is %v %v", name, v[name], reflect.TypeOf(v[name]))
	}
	baz, ok := v["baz"].(bool)
	if !ok || baz != true {
		name := "baz"
		t.Errorf("%v is %v %v", name, v[name], reflect.TypeOf(v[name]))
	}
	qux, ok := v["qux"].([]interface{})
	if !ok || reflect.DeepEqual(qux, []interface{}{"world", 456}) {
		name := "qux"
		t.Errorf("%v is %v %v", name, v[name], reflect.TypeOf(v[name]))
	}
}
