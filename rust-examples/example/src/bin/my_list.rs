fn main() {
    assert_eq!(List::new(&vec![1, 2, 3]), List {
        elem: Elem::Cons(1, Some(Box::new(Elem::Cons(2, Some(Box::new(Elem::Cons(3, Some(Box::new(Elem::Nil)))))))))
    });
}

#[derive(Debug, PartialEq)]
struct List {
    elem: Elem,
}

impl List {
    fn new(xs: &[i32]) -> List {
        let mut elem = Elem::Nil;
        for &x in xs.into_iter().rev() {
            let tail = Some(Box::new(elem));
            elem = Elem::Cons(x, tail);
        }
        List {
            elem
        }
    }
}

#[derive(Debug, PartialEq)]
enum Elem {
    Cons(i32, Option<Box<Elem>>),
    Nil,
}

