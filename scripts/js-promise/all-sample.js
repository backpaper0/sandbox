const a = new Promise(resolve => setTimeout(resolve, 1000, 'hello'))
const b = ' '
const c = new Promise(resolve => setTimeout(resolve, 200, 'world'))

// b は Promise.resolve(b) に変換される
Promise.all([a, b, c]).then(x => console.log(x.join('')))

