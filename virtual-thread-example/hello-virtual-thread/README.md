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

```bash
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

```bash
mise use java@23
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

```bash
mise use java@24
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

```bash
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

## キャリアへの固定を記録する

JFRを使用してキャリアへの固定を記録できます。

手っ取り早くJava 23でsynchronizedブロックを使用してキャリアへの固定を記録してみます。

```bash
mise use java@23
java -XX:StartFlightRecording:filename=pinned-demo.jfr,dumponexit=true App2.java
jfr print --events jdk.VirtualThreadPinned --stack-depth 100 pinned-demo.jfr
```

```
jdk.VirtualThreadPinned {
  startTime = 09:14:45.373 (2026-01-05)
  duration = 1.02 s
  eventThread = "" (javaThreadId = 34, virtual)
  stackTrace = [
    java.lang.VirtualThread.parkOnCarrierThread(boolean, long) line: 694
    java.lang.VirtualThread.park() line: 612
    java.lang.System$2.parkVirtualThread() line: 2735
    jdk.internal.misc.VirtualThreads.park() line: 54
    java.util.concurrent.locks.LockSupport.park() line: 369
    sun.nio.ch.Poller.poll(int, long, BooleanSupplier) line: 178
    sun.nio.ch.Poller.poll(int, int, long, BooleanSupplier) line: 137
    sun.nio.ch.NioSocketImpl.park(FileDescriptor, int, long) line: 175
    sun.nio.ch.NioSocketImpl.park(FileDescriptor, int) line: 201
    sun.nio.ch.NioSocketImpl.implRead(byte[], int, int) line: 309
    sun.nio.ch.NioSocketImpl.read(byte[], int, int) line: 346
    sun.nio.ch.NioSocketImpl$1.read(byte[], int, int) line: 796
    java.net.Socket$SocketInputStream.implRead(byte[], int, int) line: 1116
    java.net.Socket$SocketInputStream.read(byte[], int, int) line: 1106
    java.io.BufferedInputStream.fill() line: 291
    java.io.BufferedInputStream.read1(byte[], int, int) line: 347
    java.io.BufferedInputStream.implRead(byte[], int, int) line: 420
    java.io.BufferedInputStream.read(byte[], int, int) line: 399
    sun.net.www.http.HttpClient.parseHTTPHeader(MessageHeader, HttpURLConnection) line: 827
    sun.net.www.http.HttpClient.parseHTTP(MessageHeader, HttpURLConnection) line: 759
    sun.net.www.protocol.http.HttpURLConnection.getInputStream0() line: 1706
    sun.net.www.protocol.http.HttpURLConnection.getInputStream() line: 1615
    App2.lambda$main$0(CountDownLatch) line: 20
    java.lang.VirtualThread.run(Runnable) line: 329
    jdk.internal.vm.Continuation.enterSpecial(Continuation, boolean, boolean)
  ]
}
```

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

## スタックトレースを取得する

`jcmd`でスタックトレースを取得してみます。

CPUバウンドのデモを動かしている間に`jcmd <PID> Thread.print`を実行します。

```bash
java -Djdk.virtualThreadScheduler.maxPoolSize=1 CpuBoundsDemo.java --mode virtual --size 1
```

```bash
jcmd "$(jcmd | grep CpuBoundsDemo | awk '{print $1}')" Thread.print
```

次のようなスタックトレースが取得できました。

```
"ForkJoinPool-1-worker-1" #27 [42755] daemon prio=5 os_prio=31 cpu=7213.97ms elapsed=7.22s tid=0x000000013a97a400  [0x0000000171e49000]
   Carrying virtual thread #26
	at jdk.internal.vm.Continuation.run(java.base@25.0.1/Continuation.java:251)
	at java.lang.VirtualThread.runContinuation(java.base@25.0.1/VirtualThread.java:293)
	at java.lang.VirtualThread$$Lambda/0x00001e000101fb90.run(java.base@25.0.1/Unknown Source)
	at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.compute(java.base@25.0.1/ForkJoinTask.java:1750)
	at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.compute(java.base@25.0.1/ForkJoinTask.java:1742)
	at java.util.concurrent.ForkJoinTask$InterruptibleTask.exec(java.base@25.0.1/ForkJoinTask.java:1659)
	at java.util.concurrent.ForkJoinTask.doExec(java.base@25.0.1/ForkJoinTask.java:511)
	at java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(java.base@25.0.1/ForkJoinPool.java:1450)
	at java.util.concurrent.ForkJoinPool.runWorker(java.base@25.0.1/ForkJoinPool.java:2019)
	at java.util.concurrent.ForkJoinWorkerThread.run(java.base@25.0.1/ForkJoinWorkerThread.java:187)
   Mounted virtual thread #26
	at CpuBoundsDemo.isPrime(CpuBoundsDemo.java:140)
	at CpuBoundsDemo.countPrimesRange(CpuBoundsDemo.java:158)
	at CpuBoundsDemo.lambda$callable$0(CpuBoundsDemo.java:120)
	at CpuBoundsDemo$$Lambda/0x00001e000113c460.call(Unknown Source)
	at java.util.concurrent.FutureTask.run(java.base@25.0.1/FutureTask.java:328)
	at java.util.concurrent.ThreadPerTaskExecutor$ThreadBoundFuture.run(java.base@25.0.1/ThreadPerTaskExecutor.java:323)
	at java.lang.Thread.runWith(java.base@25.0.1/Thread.java:1487)
	at java.lang.VirtualThread.run(java.base@25.0.1/VirtualThread.java:456)
	at java.lang.VirtualThread$VThreadContinuation$1.run(java.base@25.0.1/VirtualThread.java:248)
	at jdk.internal.vm.Continuation.enter0(java.base@25.0.1/Continuation.java:325)
	at jdk.internal.vm.Continuation.enter(java.base@25.0.1/Continuation.java:316)
```

次にIOバウンドのデモを利用してアンマウントされた状態を作り出します。

```bash
java -Djdk.virtualThreadScheduler.maxPoolSize=1 IoBoundsDemo.java --mode virtual --size 1 --delay 10
```

```bash
jcmd "$(jcmd | grep IoBoundsDemo | awk '{print $1}')" Thread.print
```

次の通り仮想スレッドのスタックトレースは取得できませんでした。

```
"ForkJoinPool-1-worker-1" #27 [33283] daemon prio=5 os_prio=31 cpu=9.99ms elapsed=2.86s tid=0x0000000109650600 nid=33283 waiting on condition  [0x0000000171ce2000]
   java.lang.Thread.State: TIMED_WAITING (parking)
	at jdk.internal.misc.Unsafe.park(java.base@25.0.1/Native Method)
	- parking to wait for  <0x000000052d03bd48> (a java.util.concurrent.ForkJoinPool)
	at java.util.concurrent.ForkJoinPool.awaitWork(java.base@25.0.1/ForkJoinPool.java:2109)
	at java.util.concurrent.ForkJoinPool.deactivate(java.base@25.0.1/ForkJoinPool.java:2063)
	at java.util.concurrent.ForkJoinPool.runWorker(java.base@25.0.1/ForkJoinPool.java:2027)
	at java.util.concurrent.ForkJoinWorkerThread.run(java.base@25.0.1/ForkJoinWorkerThread.java:187)
```

`Thread.dump_to_file`コマンドであればアンマウントされた仮想スレッドのスタックトレースも確認できます。

```bash
jcmd "$(jcmd | grep IoBoundsDemo | awk '{print $1}')" Thread.dump_to_file threaddump1.txt
```

```
#26 "" virtual WAITING 2026-01-05T06:31:54.844839Z
    at java.base/java.lang.VirtualThread.park(VirtualThread.java:738)
    at java.base/java.lang.System$1.parkVirtualThread(System.java:2284)
    at java.base/java.util.concurrent.locks.LockSupport.park(LockSupport.java:367)
    at java.base/sun.nio.ch.Poller.poll(Poller.java:197)
    at java.base/sun.nio.ch.Poller.poll(Poller.java:142)
    at java.base/sun.nio.ch.NioSocketImpl.park(NioSocketImpl.java:174)
    at java.base/sun.nio.ch.NioSocketImpl.park(NioSocketImpl.java:200)
    at java.base/sun.nio.ch.NioSocketImpl.implRead(NioSocketImpl.java:307)
    at java.base/sun.nio.ch.NioSocketImpl.read(NioSocketImpl.java:354)
    at java.base/sun.nio.ch.NioSocketImpl$1.read(NioSocketImpl.java:798)
    at java.base/java.net.Socket$SocketInputStream.implRead(Socket.java:974)
    at java.base/java.net.Socket$SocketInputStream.read(Socket.java:964)
    at java.base/java.io.BufferedInputStream.fill(BufferedInputStream.java:289)
    at java.base/java.io.BufferedInputStream.read1(BufferedInputStream.java:330)
    at java.base/java.io.BufferedInputStream.read(BufferedInputStream.java:388)
    - locked <java.io.BufferedInputStream@6f477fe2>
    at java.base/sun.net.www.http.HttpClient.parseHTTPHeader(HttpClient.java:795)
    at java.base/sun.net.www.http.HttpClient.parseHTTP(HttpClient.java:727)
    at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1392)
    at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1302)
    at IoBoundsDemo.doHttpRequest(IoBoundsDemo.java:129)
    at IoBoundsDemo.lambda$callable$0(IoBoundsDemo.java:116)
    at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
    at java.base/java.util.concurrent.ThreadPerTaskExecutor$ThreadBoundFuture.run(ThreadPerTaskExecutor.java:323)
    at java.base/java.lang.VirtualThread.run(VirtualThread.java:456)
```

もちろんマウントされている仮想スレッドのスタックトレースも取得できます。

```bash
jcmd "$(jcmd | grep CpuBoundsDemo | awk '{print $1}')" Thread.dump_to_file threaddump2.txt
```

```
#26 "" virtual RUNNABLE 2026-01-05T06:33:40.332741Z
    at CpuBoundsDemo.isPrime(CpuBoundsDemo.java:140)
    at CpuBoundsDemo.countPrimesRange(CpuBoundsDemo.java:158)
    at CpuBoundsDemo.lambda$callable$0(CpuBoundsDemo.java:120)
    at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
    at java.base/java.util.concurrent.ThreadPerTaskExecutor$ThreadBoundFuture.run(ThreadPerTaskExecutor.java:323)
    at java.base/java.lang.VirtualThread.run(VirtualThread.java:456)
```
