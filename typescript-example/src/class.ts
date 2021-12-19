(() => {
    class Foo {
        constructor(private value: number) { }
    }
    class Bar {
        constructor(protected value: number) { }
    }
    class Baz {
        constructor(public value: number) { }
    }
    class Qux {
        constructor(value: number) { }
    }

    console.log(new Foo(123));
    console.log(new Bar(123));
    console.log(new Baz(123));
    console.log(new Qux(123));
})();
