fn main() {

    let foo = Bar;

    test2(&foo);
    println!("{:?}", foo);

    test3(Box::new(foo));
//    println!("{:?}", foo); // error[E0382]: borrow of moved value: `foo`
}

// fn test1(f: Foo) {} // error[E0277]: the size for values of type `(dyn Foo + 'static)` cannot be known at compilation time

fn test2(f: &Foo) {}

fn test3(f: Box<Foo>) {}

trait Foo {}

#[derive(Debug)]
struct Bar;

impl Foo for Bar {}
