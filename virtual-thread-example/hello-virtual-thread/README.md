# 仮想スレッドへ入門する

Java 21から正式版となった仮想スレッドへ入門します。

- [14 並行処理 > 仮想スレッド](https://docs.oracle.com/javase/jp/21/core/virtual-threads.html#GUID-DC4306FC-D6C1-4BCC-AECE-48C32C1A8DAA)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)

## 仮想スレッドの制御が切り替わることを確認

まず、複数の仮想スレッドを用意して制御が切り替わることを確認します（`App1.java`）。

1つのプラットフォーム・スレッド上でマウントされる仮想スレッドが切り替わることを確認したいため、システムプロパティ`jdk.virtualThreadScheduler.maxPoolSize`でプラットフォーム・スレッドの最大数を1に制限します。

```
java -Djdk.virtualThreadScheduler.maxPoolSize=1 App1.java
```

結果は次の通りです。

```
a-1
b-1
b-2
a-2
```

期待通り仮想スレッドAで`Thread.sleep`すると仮想スレッドBに制御が移っていました。

## `synchronized`ブロックで仮想スレッドがキャリアへ固定されることを確認

次に`synchronized`ブロックで仮想スレッドがキャリア（仮想スレッドがマウントされたプラットフォーム・スレッドのこと）へ固定されることを確認します。

仮想スレッドAの処理全体を`synchronized`ブロックで囲んだものを用意しました（`App2.java`）。

```java
Thread t1 = Thread.ofVirtual().unstarted(() -> {
    synchronized (gate) {
        ...
    }
});
```

先ほどと同じようにプログラムを起動します。

```
java -Djdk.virtualThreadScheduler.maxPoolSize=1 App2.java
```

結果は次の通りです。

```
a-1
a-2
b-1
b-2
```

仮想スレッドAで`Thread.sleep`しても仮想スレッドBへは制御が切り替わらず、仮想スレッドAの処理がすべて完了してから仮想スレッドBの処理が開始していました。

こちらも期待通りの動作です。
