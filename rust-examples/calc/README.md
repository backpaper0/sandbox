# calc

Rustで足し算パーサーを書いてみた。

```
$ cargo build
   Compiling calc v0.1.0 (/path/to/calc)
    Finished dev [unoptimized + debuginfo] target(s) in 0.94s
$ ./target/debug/calc "1"      
1
$ ./target/debug/calc "1 + 23"
24
$ ./target/debug/calc "1 + 23 + 456 + 7890"
8370
```

