fn main() {

    let mut bs  = [1, 2, 3, 4, 5];

    for i in &bs {
        println!("{}", i);
    }
    let mut it = bs[0..3].iter();
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());

    bs[0] = 100;
    let mut it = bs.iter();
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());
    println!("{:?}", it.next());

    test(&bs);
    println!("{:?}", bs);

}

fn test(bs: &[u8]) {
}
