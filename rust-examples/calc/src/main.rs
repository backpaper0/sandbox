extern crate calc;

use std::env;
use std::process;
use calc::Parser;

fn main() {
    let mut args = env::args();
    args.next();
    let source = args.next().unwrap();
    let mut parser = Parser::new(&source).unwrap_or_else(|err| {
        eprintln!("{}", err);
        process::exit(1);
    });
    let node = parser.parse().unwrap_or_else(|err| {
        eprintln!("{}", err);
        process::exit(1);
    });
    let result = calc::evaluate(node);
    println!("{}", result);
}
