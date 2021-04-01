use std::env;

fn main() {
    for (key, val) in env::vars() {
        println!("{}={}", key, val);
    }
}

