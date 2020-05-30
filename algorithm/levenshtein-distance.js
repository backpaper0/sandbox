// レーベンシュタイン距離

function ld(a, b) {
  if (a === '') {
    return b.length;
  }
  if (b === '') {
    return a.length;
  }
  const d = [];
  for (let i = 0; i < a.length + 1; i++) {
    const arr = new Array(b.length + 1).fill(0);
    arr[0] = i;
    d.push(arr);
  }
  for (let j = 0; j < b.length + 1; j++) {
    d[0][j] = j;
  }
  for (let i = 1; i < a.length + 1; i++) {
    for (let j = 1; j < b.length + 1; j++) {
      const cost = a[i - 1] === b[j - 1] ? 0 : 1;
      d[i][j] = Math.min(
        d[i - 1][j] + 1,
        d[i][j - 1] + 1,
        d[i - 1][j - 1] + cost
      );
    }
  }
  return d[a.length][b.length];
}

function showLD(a, b) {
  console.log(`"${a}"`, `"${b}"`, ld(a, b));
}

showLD('kitten', 'sitting');
showLD('ab', 'abc');
showLD('abcdef', 'bcdefg');
showLD('', 'abc');
showLD('abc', '');
showLD('', '');
