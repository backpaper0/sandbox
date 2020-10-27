const text = '𩸽を食べたい'; //見た目は6文字

console.log(text);

console.log(text.length, 'String.length'); // 7

console.log([...text].length, 'スプレッド演算子でコードポイントの配列にする'); //6

console.log((() => {
  let size = 0;
  for (const i of text) {
    size++;
  }
  return size;
})(), 'for ofでコードポイントをイテレートする'); //6
