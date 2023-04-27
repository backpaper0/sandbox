package cnst2

import (
	"sandbox/cnst/cnst1"
	"testing"
)

func TestIllegalConst(t *testing.T) {
	// 型がエクスポートされていなければ不正な値が作成されることはない
	// （もちろんパッケージ内で不正な値を作成することはあり得る）
	// illegalConstA := cnst1.myConstA(100) // myConstA not exported by package cnst1

	// 型がエクスポートされていると不正な値が作成され得る
	illegalConstB := cnst1.MyConstB(100)
	if illegalConstB == cnst1.MyEnumB1 {
		t.Fail()
	} else if illegalConstB == cnst1.MyEnumB2 {
		t.Fail()
	} else if illegalConstB == cnst1.MyEnumB3 {
		t.Fail()
	}
}
