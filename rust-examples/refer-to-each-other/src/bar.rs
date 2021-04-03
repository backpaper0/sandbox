use crate::foo;

pub fn run(i: i32) -> i32 {
    if i == 0 {
        0
    } else {
        i + foo::run(i - 1)
    }
}

