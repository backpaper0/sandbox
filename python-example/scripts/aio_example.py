"""
非同期IOでプロデューサーコンシューマーパターンを実装してみる。
ファイルから値を読み取り、システム時刻を付与して別のファイルへ書き出す。

動作確認するためにはまず読み取りファイルを準備する。

```
python -m scripts.aio_example --init
```

次に処理を実行する。`parallels`パラメーターで並列度を指定できる。

```
python -m scripts.aio_example --parallels=4
```
"""

import argparse
import asyncio
import json
from datetime import datetime
from pathlib import Path

import aiofiles
from tqdm import tqdm, trange

_input_path = Path("data") / "aio_example_input.jsonl"
_output_path = Path("data") / "aio_example_output.jsonl"


_size = 60


async def _init() -> None:
    async with aiofiles.open(_input_path, mode="w", encoding="utf-8") as output:
        for i in trange(_size):
            json_line = json.dumps(
                {
                    "value": i,
                }
            )
            await output.write(json_line)
            await output.write("\n")


async def _read_file(
    input_queue: asyncio.Queue,
    bar: tqdm,
) -> None:
    async with aiofiles.open(_input_path, mode="r", encoding="utf-8") as input:
        while True:
            line = await input.readline()
            if not line:
                break
            item = json.loads(line)
            await input_queue.put(item)
            bar.update()


async def _process(
    input_queue: asyncio.Queue,
    output_queue: asyncio.Queue,
    bar: tqdm,
) -> None:
    while True:
        item = await input_queue.get()
        if item is None:
            input_queue.task_done()
            break
        value = item["value"]
        current_time = datetime.now().strftime("%H:%M:%S")
        await asyncio.sleep(1)
        await output_queue.put({"value": f"{current_time} {value}"})
        input_queue.task_done()
        bar.update()


async def _write_file(
    output_queue: asyncio.Queue,
    bar: tqdm,
) -> None:
    async with aiofiles.open(_output_path, mode="w", encoding="utf-8") as output:
        while True:
            item = await output_queue.get()
            if item is None:
                output_queue.task_done()
                break
            line = json.dumps(item, ensure_ascii=False)
            await output.write(line)
            await output.write("\n")
            output_queue.task_done()
            bar.update()


async def _main(parallels: int) -> None:
    input_queue: asyncio.Queue = asyncio.Queue(parallels)
    output_queue: asyncio.Queue = asyncio.Queue(parallels)

    with (
        tqdm(total=_size, desc="   read") as read_bar,
        tqdm(total=_size, desc="process") as process_bar,
        tqdm(total=_size, desc="  write") as write_bar,
    ):
        read_file_task = asyncio.create_task(
            _read_file(
                input_queue=input_queue,
                bar=read_bar,
            )
        )
        process_task = asyncio.gather(
            *[
                asyncio.create_task(
                    _process(
                        input_queue=input_queue,
                        output_queue=output_queue,
                        bar=process_bar,
                    )
                )
                for _ in range(parallels)
            ]
        )
        write_file_task = asyncio.create_task(
            _write_file(
                output_queue=output_queue,
                bar=write_bar,
            )
        )

        await read_file_task

        for _ in range(parallels):
            await input_queue.put(None)

        await process_task

        await output_queue.put(None)

        await write_file_task


async def main(
    init: bool,
    parallels: int,
) -> None:
    if init:
        await _init()
        return
    await _main(parallels=parallels)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--init", action="store_true")
    parser.add_argument("--parallels", type=int, default=1)
    args = parser.parse_args()

    asyncio.run(
        main(
            init=args.init,
            parallels=args.parallels,
        )
    )
