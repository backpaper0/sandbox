const immutable = require('immutable');

const Record = immutable.Record;

class Foo extends Record({ bar: 0}) {
}

class Hoge extends Record({ foo: new Foo() }) {
}

const a = new Hoge();

console.log(a.foo.bar);

const b = a.setIn(['foo', 'bar'], 123);

console.log(b.foo.bar);
