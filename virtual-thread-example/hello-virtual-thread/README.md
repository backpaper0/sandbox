# 仮想スレッドへ入門する

Java 21から正式版となった仮想スレッドへ入門します。

- [Virtual Threads](https://docs.oracle.com/en/java/javase/25/core/virtual-threads.html)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)

## 準備

HTTPBinをDockerで立てておきます。

```bash
docker run -d --name httpbin -p 8080:80 kennethreitz/httpbin
```

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
a-2
```

期待通り仮想スレッドAでHTTPリクエストを行うと仮想スレッドBに制御が移っていました。

## `synchronized`ブロックで仮想スレッドがキャリアへ固定されることを確認(〜Java 23)

次にバージョン23以下のJavaを使って`synchronized`ブロックで仮想スレッドがキャリア（仮想スレッドがマウントされたプラットフォーム・スレッドのこと）へ固定されることを確認します。

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
asdf local java liberica-23+38
java -Djdk.virtualThreadScheduler.maxPoolSize=1 App2.java
```

結果は次の通りです。

```
a-1
a-2
b-1
```

仮想スレッドAで`Thread.sleep`しても仮想スレッドBへは制御が切り替わらず、仮想スレッドAの処理がすべて完了してから仮想スレッドBの処理が開始していました。

Java 24以上でも動かして`synchronized`ブロックではキャリアへ固定されないことを確認しましょう。

```
asdf local java liberica-24+37
java -Djdk.virtualThreadScheduler.maxPoolSize=1 App2.java
```

結果は次の通りです。

```
a-1
b-1
a-2
```

こちらも期待通りの動作です。

## nativeメソッドによる固定化

`FileInputStream.readAllBytes()`が呼ばれたときに仮想スレッドが切り替わるか確認してみましたが、切り替わりませんでした。

```
java -Djdk.virtualThreadScheduler.maxPoolSize=1 App3.java
```

```
a-1
a-2
b-1
```

これは`FileInputStream.readAllBytes()`から辿って呼び出されている`FileInputStream.readBytes()`がnativeメソッドであることが要因です。

> ```java
> private native int readBytes(byte[] b, int off, int len) throws IOException;
> ```

(Java 23までの)`synchronized`ブロックと同様にnativeメソッドも仮想スレッドをキャリアへ固定します。

> 仮想スレッドがキャリアに固定されている場合、ブロッキング操作中にマウント解除することはできません。仮想スレッドが固定されるのは次の状況です:
> 
> - 仮想スレッドが、synchronizedブロックまたはメソッド内でコードを実行します
> - 仮想スレッドが、nativeメソッドまたは外部関数実行します([外部関数およびメモリーAPI](https://docs.oracle.com/javase/jp/21/core/foreign-function-and-memory-api.html#GUID-FBE990DA-C356-46E8-9109-C75567849BA8)を参照)

- [仮想スレッドのスケジュールおよび固定された仮想スレッド](https://docs.oracle.com/javase/jp/21/core/virtual-threads.html#GUID-704A716D-0662-4BC7-8C7F-66EE74B1EDAD)より引用

[Java 24のドキュメント](https://docs.oracle.com/en/java/javase/24/core/virtual-threads.html#GUID-704A716D-0662-4BC7-8C7F-66EE74B1EDAD)を読むと、これまで述べたように`synchronized`ブロックは固定の条件から削除されたことがわかります。

## IOバウンドな処理の性能向上

次の条件でそれぞれプログラムを実行して性能向上するかどうかを確認します。

- シングルスレッド
- マルチスレッド（プラットフォームスレッド）
    - プラットフォームスレッド数: 4
- 仮想スレッド（キャリアになるプラットフォームスレッドの最大数が1）
    - 仮想スレッド数: 4
    - キャリアになるプラットフォームスレッド数: 1

```bash
java IoBoundsDemo.java --mode single
```

```
経過時間: 12.063秒
```

```bash
java IoBoundsDemo.java --mode platform
```

```
経過時間: 3.026秒
```

```bash
java -Djdk.virtualThreadScheduler.maxPoolSize=1 IoBoundsDemo.java --mode virtual
```

```
経過時間: 3.032秒
```
