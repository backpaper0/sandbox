# 非同期IO

- https://docs.python.org/ja/3.12/library/asyncio.html

`async def`でコルーチンを作成できる。

```bash
python -m scripts.asyncio.example1
```

コルーチンは`await`で実行できる。

```bash
python -m scripts.asyncio.example2
```

```
21:24:28 start
21:24:30 hello
21:24:31 world
21:24:31 end
```

`asyncio.create_task()`はコルーチンをスケジュールできる。

```bash
python -m scripts.asyncio.example3
```

```
21:24:34 start
21:24:35 world
21:24:36 hello
21:24:36 end
```

`asyncio.gather()`で複数のコルーチンを束ねられる。

```bash
python -m scripts.asyncio.example4
```

```
21:29:58 start
21:30:00 21:30:00 hello
21:30:00 21:29:59 world
21:30:00 end
```

`asyncio.Lock`でロックできる。

```bash
python -m scripts.asyncio.example5
```

```
21:34:45 start
21:34:47 hello
21:34:48 world
21:34:48 end
```

セマフォ（`asyncio.Semaphore()`）もある。

```bash
python -m scripts.asyncio.example6
```

```
21:39:06 start
21:39:07 msg#1
21:39:07 msg#2
21:39:07 msg#3
21:39:08 msg#4
21:39:08 msg#5
21:39:08 msg#6
21:39:09 msg#7
21:39:09 msg#8
21:39:09 msg#9
21:39:09 end
```

`asyncio.Event`でブロックできる。

```bash
python -m scripts.asyncio.example7
```

```
04:47:41 start
04:47:41 ready... #1
04:47:41 ready... #2
04:47:41 ready... #3
04:47:44 go! #1
04:47:44 go! #2
04:47:44 go! #3
04:47:44 end
```

`asyncio.Barrier`を試す。

```bash
python -m scripts.asyncio.example7
```

```
04:54:51 start
04:54:51 ready... #1 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [filling, waiters:0/3]>
04:54:52 ready... #2 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [filling, waiters:1/3]>
04:54:53 ready... #3 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [filling, waiters:2/3]>
04:54:53 go! #3 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [draining, waiters:0/3]>
04:54:53 go! #1 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [draining, waiters:0/3]>
04:54:53 go! #2 barrier=<asyncio.locks.Barrier object at 0x1011a9050 [filling, waiters:0/3]>
04:54:53 end
```
