"""
非同期IOまわりの処理をまとめたモジュール。
"""

import asyncio
from pathlib import Path
from typing import Optional

import aiofiles
from tqdm import tqdm


async def file_reader(
    input_file: Path,
    input_queue: asyncio.Queue,
    progress_bar: Optional[tqdm] = None,
) -> None:
    """
    ファイルを読み込んで、行ごとにキューに入れます。
    Args:
        input_file (Path): 入力ファイルのパス
        input_queue (asyncio.Queue): 入力キュー
        progress_bar (Optional[tqdm]): 進捗バー
    Returns:
        None
    """
    async with aiofiles.open(input_file, encoding="utf-8", mode="r") as f:
        async for line in f:
            await input_queue.put(line)
            if progress_bar is not None:
                progress_bar.update()
    await input_queue.put(None)


async def file_writer(
    output_file: Path,
    output_queue: asyncio.Queue,
    progress_bar: Optional[tqdm] = None,
) -> None:
    """
    キューから行を取り出してファイルに書き込みます。
    Args:
        output_file (Path): 出力ファイルのパス
        output_queue (asyncio.Queue): 出力キュー
        progress_bar (Optional[tqdm]): 進捗バー
    Returns:
        None
    """
    async with aiofiles.open(output_file, encoding="utf-8", mode="w") as f:
        while True:
            line = await output_queue.get()
            if not line:
                output_queue.task_done()
                break
            await f.write(line)
            await f.write("\n")
            output_queue.task_done()
            if progress_bar is not None:
                progress_bar.update()
