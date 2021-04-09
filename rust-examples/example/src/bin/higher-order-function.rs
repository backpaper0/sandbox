// https://doc.rust-jp.rs/book-ja/ch19-05-advanced-functions-and-closures.html

fn main() {
    let i = receive_function("hello".to_string(), |s| s.len() as i32);
    println!("{}", i);

    let f = return_function(12);
    println!("{}", f(34));
}

fn receive_function(s: String, f: fn(String) -> i32) -> i32 {
    f(s)
}

fn return_function(x: i32) -> Box<dyn Fn(i32) -> i32> {
    Box::new(move |y| x + y)
}
