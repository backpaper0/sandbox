use crate::bar;

pub fn run(i: i32) -> i32 {
    if i == 0 {
        0
    } else {
        i + bar::run(i - 1)
    }
}

