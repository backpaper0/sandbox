fn main() {
    let a = String::from("hello");
    run1(&a).unwrap();
    run2(&a).unwrap();
}

fn run1(a: &String) -> Result<(), String> {
    let b = match foo(a) {
        Ok(x) => x,
        Err(x) => return Err(x),
    };
    let c = match bar(a) {
        Ok(x) => x,
        Err(x) => return Err(x),
    };
    let d = match baz(&b, c) {
        Ok(x) => x,
        Err(x) => return Err(x),
    };
    println!("{:?}", d);
    Ok(())
}

fn run2(a: &String) -> Result<(), String> {
    let b = foo(a)?;
    let c = bar(a)?;
    let d = baz(&b, c)?;
    println!("{:?}", d);
    Ok(())
}

fn foo(s: &String) -> Result<&String, String> {
    if s.is_empty() {
        Err(String::from("foo"))
    } else {
        Ok(s)
    }
}

fn bar(s: &String) -> Result<i32, String> {
    if s.is_empty() {
        Err(String::from("bar"))
    } else {
        Ok(s.len() as i32)
    }
}

fn baz(s: &String, i: i32) -> Result<(&String, i32), String> {
    if s.is_empty() || i == 0 {
        Err(String::from("baz"))
    } else {
        Ok((s, i))
    }
}

