import hello from './hello'
import obj from './object'
import defaultValue from './default-value'

console.log(hello('world'))

console.log(obj.name)
console.log(obj.data().foo)
console.log(obj.data().bar)

console.log(defaultValue())
console.log(defaultValue('backpaper0'))

//compile error
//console.log(hello(123))
