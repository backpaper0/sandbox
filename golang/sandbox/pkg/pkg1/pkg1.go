package pkg1

type Type1 string

type type2 string

func Func1() {
}

func func2() {
}

func (t *Type1) Method1() {
}

func (t *Type1) method2() {
}

var Var1 string = "var1"

var var2 string = "var2"

const (
	Const1 string = "const1"
	const2 string = "const2"
)

type Struct0 struct {
	Field1 string
	field2 string
}

type Interface1 interface {
	Method1()
	method2()
}

type notExportedType struct{}

func (a *notExportedType) ExportedMethod() {
}

func ExportedFunc() notExportedType {
	return notExportedType{}
}
