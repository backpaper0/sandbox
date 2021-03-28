fn main() {
    let xs: Vec<i32> = vec![1, 2, 3, 4, 5, 6, 7, 8, 9];
    let ys: &[i32] = &xs[1..xs.len() - 1];
    let zs: &[i32] = &ys[1..ys.len() - 1];

    println!("{:?}", xs);
    println!("{:?}", ys);
    println!("{:?}", zs);

    println!("{}", sum(&xs));
}

fn sum(xs: &[i32]) -> i32 {
    if xs.len() > 0 {
        xs[0] + sum(&xs[1..])
    } else {
        0
    }
}
