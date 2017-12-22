fn main() {
    println!("Hello, world!");

    let x = 5;

    let (x, y) = (1, 2);

    let x: i32 = 5;

    let mut x = 5;

    x = 10;

    let x: i32;

    let x = 8;
    {
        println!("{}", x);
        let x = 12;
        println!("{}", x);
    }
    println!("{}", x);
    let x = 42;
    println!("{}", x);
    let x = "HELLO WORLD";
    println!("{}", x);
}
