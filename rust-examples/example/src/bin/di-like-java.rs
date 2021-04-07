fn main() {
    let foo = FooImpl;
    let bar = Bar::new(&foo);
    let s = bar.get();
    println!("{}", s);
}

trait Foo {
    fn get(&self) -> String;
}

struct Bar<'a> {
    foo: &'a dyn Foo,
}

impl<'a> Bar<'a> {
    fn new(foo: &'a dyn Foo) -> Bar<'a> {
        Bar {
            foo
        }
    }
    fn get(&self) -> String {
        format!("{}bar", self.foo.get())
    }
}

struct FooImpl;

impl Foo for FooImpl {
    fn get(&self) -> String {
        "foo".to_string()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    struct MockFoo;

    impl Foo for MockFoo {
        fn get(&self) -> String {
            "mock".to_string()
        }
    }

    #[test]
    fn test() {
        let foo = MockFoo;
        let bar = Bar::new(&foo);
        assert_eq!(bar.get(), "mockbar");
    }
}
