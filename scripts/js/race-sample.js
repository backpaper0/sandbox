const a = new Promise(resolve => setTimeout(resolve, Math.random() * 1000, 'foo'))
const b = new Promise(resolve => setTimeout(resolve, Math.random() * 1000, 'bar'))
const c = new Promise(resolve => setTimeout(resolve, Math.random() * 1000, 'baz'))

Promise.race([a, b, c]).then(x => console.log(x))

