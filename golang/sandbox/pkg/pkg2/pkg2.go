package pkg2

import (
	"fmt"
	"sandbox/pkg/pkg1"
)

func test() {
	// 名前が小文字はじまりの場合はエクスポートされない。

	t1 := pkg1.Type1("type1")
	// t2 := pkg1.type2("type2") // type2 not exported by package pkg1

	t1.Method1()
	// t1.method2() // t1.method2 undefined (type pkg1.Type1 has no field or method method2)

	var i pkg1.Interface1 = &t1
	i.Method1()
	// i.method2() // i.method2 undefined (type pkg1.Interface1 has no field or method method2)

	pkg1.Func1()
	// pkg1.func2() // func2 not exported by package pkg1

	pkg1.Var1 = "var1"
	// pkg1.var2 = "var2" // var2 not exported by package pkg1

	c1 := pkg1.Const1
	// c2 := pkg1.const2 // const2 not exported by package pkg1
	fmt.Println(c1)

	st := pkg1.Struct0{}
	st.Field1 = "field1"
	// st.field2 = "field2" // st.field2 undefined (type pkg1.Struct0 has no field or method field2)
}
