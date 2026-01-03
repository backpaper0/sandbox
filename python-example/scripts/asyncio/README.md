# 非同期IO

- https://docs.python.org/ja/3.12/library/asyncio.html

`async def`でコルーチンを作成できる。

```bash
uv run -m scripts.asyncio.example1
```

コルーチンは`await`で実行できる。

```bash
uv run -m scripts.asyncio.example2
```

```
21:24:28 start
21:24:30 hello
21:24:31 world
21:24:31 end
```

`asyncio.create_task()`はコルーチンをスケジュールできる。

```bash
uv run -m scripts.asyncio.example3
```

```
21:24:34 start
21:24:35 world
21:24:36 hello
21:24:36 end
```

`asyncio.gather()`で複数のコルーチンを束ねられる。

```bash
uv run -m scripts.asyncio.example4
```

```
21:29:58 start
21:30:00 21:30:00 hello
21:30:00 21:29:59 world
21:30:00 end
```

`asyncio.Lock`でロックできる。

```bash
uv run -m scripts.asyncio.example5
```

```
21:34:45 start
21:34:47 hello
21:34:48 world
21:34:48 end
```

セマフォ（`asyncio.Semaphore()`）もある。

```bash
uv run -m scripts.asyncio.example6
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
uv run -m scripts.asyncio.example7
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
uv run -m scripts.asyncio.example8
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

`asyncio.Queue`を試す。

```bash
uv run -m scripts.asyncio.example9
```

```
05:01:10 start
05:01:13 group#1
  05:01:11 task#1
  05:01:12 task#2
05:01:16 group#2
  05:01:13 task#3
  05:01:14 task#4
  05:01:15 task#5
  05:01:16 task#6
05:01:19 group#3
  05:01:17 task#7
  05:01:18 task#8
  05:01:19 task#9
05:01:22 group#4
  05:01:20 task#10
05:01:22 end
```

`loop.run_in_executor()`を使うとブロッキングIOを`async/await`へ対応させられる。

```bash
uv run -m scripts.asyncio.example10
```

```
[Blocking IO]results: 0, 2, 6, 12, 20 (5.02 sec)
[async/await]results: 0, 2, 6, 12, 20 (1.01 sec)
```