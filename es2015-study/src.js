const obj = {
  foo: [1, 2, 3],
  bar: [4, 5, 6],
  baz: [7, 8, 9]
};

const a = Object.values(obj).map(x => x[0]);

console.log(a);


const arr = ['foo', 'bar'];

arr.forEach(x => console.log(x));

class Person {
  constructor(name) {
    this._name = name
  }
  get name() { return this._name }
  hello() {
    return 'Hello, ' + this.name + '!'
  }
}

const p = new Person('backpaper0');
console.log(p.name);
console.log(p.hello());

const HogeMixin = x => class extends x {
  showThis() {
    console.log(this)
  }
}
class Foo {}
class Bar extends HogeMixin(Foo) {}

const bar = new Bar();
bar.showThis();
