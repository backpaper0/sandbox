fn main() {
    assert_eq!(my_list(&vec![1, 2, 3]), List::Cons(1, Some(Box::new(List::Cons(2, Some(Box::new(List::Cons(3, Some(Box::new(List::Nil))))))))));
}

#[derive(Debug, PartialEq)]
enum List {
    Cons(i32, Option<Box<List>>),
    Nil,
}

fn my_list(xs: &[i32]) -> List {
    let mut list = List::Nil;
    for &x in xs.into_iter().rev() {
        let tail = Some(Box::new(list));
        list = List::Cons(x, tail);
    }
    list
}

