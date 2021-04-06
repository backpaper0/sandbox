use split_sourcecode::{foo, qux};

fn main() {
    let s = foo::get();
    println!("{}", s);

    let s = qux::get();
    println!("{}", s);
}
