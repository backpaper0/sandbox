function* f(){
  yield 1
  yield 2
  yield 3
}

let g = f()

for (let i of g) {
  console.log(i);
}
