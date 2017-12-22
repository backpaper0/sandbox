fn main() {
    let v1 = vec![1, 2, 3];
    let v2 = vec![4, 5, 6];

//    let answer = foo(v1, v2);
    let answer = foo(&v1, &v2);

    println!("{:?}", v1);
    println!("{:?}", v2);

    let v3 = &v1;
    println!("{:?}", v3);

    let mut x = 5;
    {
        let y = &mut x;
        *y += 1;
    }
    println!("{}", x);
    let y = &mut x;
    *y += 1;
    //error[E0502]: cannot borrow `x` as immutable because it is also borrowed as mutable
//    println!("{}", x);
}

//fn foo(v1: Vec<i32>, v2: Vec<i32>) -> i32 {
fn foo(v1: &Vec<i32>, v2: &Vec<i32>) -> i32 {
//    v1.push(100);
    42
}
