extern crate calc;

use std::env;
use calc::Parser;

fn main() {
    let mut args = env::args();
    args.next();
    let source = args.next().unwrap();
    let mut parser = Parser::new(&source);
    let node = parser.parse();
    let result = calc::evaluate(node);
    println!("{}", result);
}

