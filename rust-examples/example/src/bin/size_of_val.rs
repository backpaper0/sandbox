use std::mem;

// bool: 1
// i32: 4
// Point1: 16
// Point2: 16
// Scalar: 8
// Dir: 1
// String: 24
// &String: 8
// &i32: 8
// &Option<String>: 8
// &Option<i32>: 8
// Option<String>: 24
// Option<i32>: 8
// Box<i32>: 8
// Box<i64>: 8

fn main() {
    println!("bool: {}", mem::size_of::<bool>());
    println!("i32: {}", mem::size_of::<i32>());
    println!("Point1: {}", mem::size_of::<Point1>());
    println!("Point2: {}", mem::size_of::<Point2>());
    println!("Scalar: {}", mem::size_of::<Scalar>());
    println!("Dir: {}", mem::size_of::<Dir>());
    println!("String: {}", mem::size_of::<String>());
    println!("&String: {}", mem::size_of::<&String>());
    println!("&i32: {}", mem::size_of::<&i32>());
    println!("&Option<String>: {}", mem::size_of::<&Option<String>>());
    println!("&Option<i32>: {}", mem::size_of::<&Option<i32>>());
    println!("Option<String>: {}", mem::size_of::<Option<String>>());
    println!("Option<i32>: {}", mem::size_of::<Option<i32>>());
    println!("Box<i32>: {}", mem::size_of::<Box<i32>>());
    println!("Box<i64>: {}", mem::size_of::<Box<i64>>());
}

struct Point1 {
    x: f64,
    y: f64,
}

struct Point2(f64, f64);

enum Scalar {
    Num(i32),
    Bool(bool),
}

enum Dir {
    Left,
    Right,
}

