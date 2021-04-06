use crate::bar;

pub fn get() -> String {
    let s = bar::get();
    format!("hello foo{}", s).to_string()
}
