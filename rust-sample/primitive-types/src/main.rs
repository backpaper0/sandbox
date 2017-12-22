fn main() {

    let x = true;
    println!("{}", x);

    let y: bool = false;
    println!("{}", y);

    let x = 'x';
    println!("{}", x);

    let sushi = '🍣';
    println!("{}", sushi);

    let x = 42;
    println!("{}", x);

    let y = 1.0;
    println!("{}", y);

    let a = [1, 2, 3];
    println!("{:?}", a);
    println!("{}", a.len());
    println!("{}", a[1]);

    let a = [0; 20];
    println!("{:?}", a);
    println!("{}", a.len());
    println!("{}", a[1]);

    let a = [0, 1, 2, 3, 4];
    let complete = &a[..];
    let middle = &a[1..4];
    println!("{:?}", a);
    println!("{:?}", complete);
    println!("{:?}", middle);

    let x = (1, "hello");
    println!("{:?}", x);
    println!("{}", x.0);
    println!("{}", x.1);
}
