import hello from './hello'
import obj from './object'

console.log(hello('world'))

console.log(obj.name)
console.log(obj.data().foo)
console.log(obj.data().bar)

//compile error
//console.log(hello(123))
