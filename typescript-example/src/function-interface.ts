
interface Calc {
  (a: number, b: number): number;
}

const add: Calc = (a, b) => a + b;

console.log('1 + 2 =', add(1, 2));


interface Foo {
  (x: number): number;
  foo?(x: number): number;
  bar?: number;
}

const foo1: Foo = x => x;
console.log('foo1', foo1(1));

const foo2: Foo = x => x;
foo2.foo = x => x;
foo2.bar = 123;
console.log('foo2', foo2(1));
console.log('foo2.foo', foo2.foo(2));
console.log('foo2.bar', foo2.bar);
