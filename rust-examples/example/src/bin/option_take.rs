fn main() {

    let s = String::from("hello");
    let mut x = Some(s);
    let mut y: Option<String> = None;

    // println!("{}", s); // error[E0382]: borrow of moved value: `s`

    // y = x;
    // println!("{:?}", x); // error[E0382]: borrow of moved value: `x`

    y = x.take();
    println!("{:?}", x);
    println!("{:?}", y);
}

struct MyContainer {
    value: String,
}

impl MyContainer {
    fn new(value: String) -> Self {
        Self {
            value
        }
    }
}

