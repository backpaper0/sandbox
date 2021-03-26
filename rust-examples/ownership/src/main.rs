fn main() {

    let v = vec![1, 2, 3];

    let v2 = v;

    //error[E0382]: use of moved value: `v`
//    println!("v[0] is: {}", v[0]);

    //Copy型
    let v = 1;

    let v2 = v;

    println!("v is: {}", v);
}
