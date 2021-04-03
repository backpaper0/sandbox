use std::env;

fn main() {
    let a: i32 = env::args().nth(0).unwrap().parse::<i32>().unwrap();
    let b = match a {
        1 => true,
        2 => true,
        3 | 4 => true,
        _ => false,
    };
    println!("{}", b);
}
