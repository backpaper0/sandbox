fn main() {
    print_number(5);
    print_number(add_one(5));

    let mut y = 5;

    let x = (y = 6);

    let f: fn(i32) -> i32;

    let f = add_one;
    print_number(f(f(5)));

    oops();
}

fn oops() -> ! {
    panic!("x_x");
}

fn foo(x: i32) -> i32 {
    return x;

    x + 1
}

fn print_number(x: i32) {
    println!("x is: {}", x);
}

fn add_one(x: i32) -> i32 {
    x + 1
}
