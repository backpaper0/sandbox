use std::str::Chars;

fn main() {
    let mut s: String = String::from("hello");
    let mut cs: Chars = s.chars();
    let c: Option<char> = cs.next();
    println!("{:?}", c);
    println!("{}", s);

    let c: Option<char> = head(&s);
    println!("{:?}", c);
    println!("{}", s);

    let mut cs: Chars = chars(&s);
    let c: Option<char> = cs.next();
    println!("{:?}", c);
    println!("{}", s);
}

fn head(s: &String) -> Option<char> {
    let mut cs: Chars = s.chars();
    let c: Option<char> = cs.next();
    c
}

fn chars<'a>(s: &'a String) -> Chars<'a> {
    s.chars()
}

