use std::env;

fn main() {
    println!("Begin main");
    let _foo = Foo(1);
    bar();
    println!("End main");
}

fn bar() {
    println!("Begin bar");
    let _foo = Foo(2);
    let len = env::args().len();
    println!("Args length: {}", len);
    if len == 1 {
        panic!("Panic");
    }
    println!("End bar");
}


struct Foo(i32);

impl Drop for Foo {
    fn drop(&mut self) {
        println!("Drop Foo({})", self.0);
    }
}
