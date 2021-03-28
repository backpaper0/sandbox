fn main() {
    let xs: Vec<i32> = vec![1, 2, 3, 4, 5, 6, 7, 8, 9];
    let ys: &[i32] = &xs[1..xs.len() - 1];
    let zs: &[i32] = &ys[1..ys.len() - 1];

    println!("{:?}", xs);
    println!("{:?}", ys);
    println!("{:?}", zs);

    println!("{}", sum(&xs));

    //--------------------------------------------------
    //Copyトレイトを実装しているi32のVec
    let a = 1;
    let xs = vec![a];
    println!("{:?}", xs);

    //Vec構築の際、所有権は移動されず値がコピーされるだけなので、ここでも使用できる
    println!("{}", a);

    //所有権を移動するのではなく、値がコピーされるだけ
    let b = xs[0];
    println!("{}", b);

    for x in xs.iter() {
        let y: &i32 = x;
        println!("{}", y);
    }
    for x in &xs {
        let y: &i32 = x;
        println!("{}", y);
    }
    for x in xs { //所有権が移動するため、これ以降はxsは使えない
        let y: i32 = x;
        println!("{}", y);
    }

    //--------------------------------------------------
    //Copyトレイトを実装していないStringのVec
    let a = String::from("foo");
    let xs = vec![a];
    println!("{:?}", xs);

    //所有権がxsへ移っているため、ここでは使用できない
    //error[E0382]: borrow of moved value: `a`
    //move occurs because `a` has type `String`, which does not implement the `Copy` trait
    //println!("{}", a);

    //所有権は移動できない
    //error[E0507]: cannot move out of index of `Vec<String>`
    //move occurs because value has type `String`, which does not implement the `Copy` trait
    //let b = xs[0];

    for x in xs.iter() {
        let y: &String = x;
        println!("{}", y);
    }
    for x in &xs {
        let y: &String = x;
        println!("{}", y);
    }
    for x in xs { //所有権が移動するため、これ以降はxsは使えない
        let y: String = x;
        println!("{}", y);
    }
}

fn sum(xs: &[i32]) -> i32 {
    if xs.len() > 0 {
        xs[0] + sum(&xs[1..])
    } else {
        0
    }
}
