package snippet

// 配列を反転させる
func ReverseArray(a []int) {
	n := len(a)
	for i, m := 0, n/2; i < m; i++ {
		j := n - i - 1
		a[i], a[j] = a[j], a[i]
	}
}
