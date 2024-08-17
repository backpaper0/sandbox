"""
テキストの埋め込みを生成するためのモジュールです。
"""

import asyncio
import json
from pathlib import Path
from typing import Optional, Tuple

import aiofiles
import aiosqlite
from tqdm import tqdm

from app.cache import CacheManager
from app.embeddings_client import EmbeddingsClient, get_embeddings_client


async def _reader(
    input_file: Path,
    input_queue: asyncio.Queue,
    progress_bar: Optional[tqdm] = None,
) -> None:
    async with aiofiles.open(input_file, encoding="utf-8", mode="r") as f:
        async for line in f:
            await input_queue.put(line)
            if progress_bar is not None:
                progress_bar.update()
    await input_queue.put(None)


async def _processor(
    embeddings_client: EmbeddingsClient,
    input_queue: asyncio.Queue,
    output_queue: asyncio.Queue,
    text_column: str,
    vector_column: str,
    conn: aiosqlite.Connection,
    progress_bar: Optional[tqdm] = None,
) -> int:
    async with conn.cursor() as cursor:

        cache = CacheManager(cursor)

        async def vectorize(text: str) -> Tuple[list[float], bool]:
            vector = await cache.get(text)
            if vector is not None:
                return vector, True
            vector = await embeddings_client(text)
            await cache.set(text, vector)
            return vector, False

        hit_cache_count = 0
        while True:
            line = await input_queue.get()
            if line is None:
                input_queue.task_done()
                break
            item = json.loads(line)
            text = item[text_column]
            vector, hit_cache = await vectorize(text)
            if hit_cache:
                hit_cache_count += 1
            item[vector_column] = vector
            line = json.dumps(item, ensure_ascii=False)
            await output_queue.put(line)
            input_queue.task_done()
            if progress_bar is not None:
                progress_bar.update()

        await conn.commit()

    return hit_cache_count


async def _writer(
    output_file: Path,
    output_queue: asyncio.Queue,
    progress_bar: Optional[tqdm] = None,
) -> None:
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


async def embeddings(
    input_file: Path,
    output_file: Path,
    parallels: int,
    show_progress_bar: bool,
    conn: aiosqlite.Connection,
    text_column: str,
    vector_column: str,
) -> None:
    """
    埋め込みを生成します。

    Args:
        input_file (Path): 入力ファイルのパス
        output_file (Path): 出力ファイルのパス
        parallels (int): 並列処理の数
        show_progress_bar (bool): 進捗バーを表示するかどうかのフラグ
        conn (aiosqlite.Connection): データベース接続オブジェクト
        text_column (str): 元テキストのカラム名
        vector_column (str): 埋め込みのカラム名

    Returns:
        None
    """

    embeddings_client = get_embeddings_client()

    if show_progress_bar:
        with open(input_file, encoding="utf-8", mode="r") as f:
            total = sum(1 for _ in f)
        progress_bar_for_reader = tqdm(total=total)
        progress_bar_for_processor = tqdm(total=total)
        progress_bar_for_writer = tqdm(total=total)
    else:
        progress_bar_for_reader = None
        progress_bar_for_processor = None
        progress_bar_for_writer = None

    input_queue: asyncio.Queue = asyncio.Queue(parallels)
    output_queue: asyncio.Queue = asyncio.Queue(parallels)

    reader_task = asyncio.create_task(
        _reader(input_file, input_queue, progress_bar_for_reader)
    )
    processor_task = asyncio.gather(
        *[
            _processor(
                embeddings_client=embeddings_client,
                input_queue=input_queue,
                output_queue=output_queue,
                text_column=text_column,
                vector_column=vector_column,
                conn=conn,
                progress_bar=progress_bar_for_processor,
            )
            for _ in range(parallels)
        ]
    )
    writer_task = asyncio.create_task(
        _writer(output_file, output_queue, progress_bar_for_writer)
    )

    await reader_task
    for _ in range(parallels):
        await input_queue.put(None)
    hit_cache_count_list = await processor_task
    await output_queue.put(None)
    await writer_task

    progress_bar_for_reader.close()
    progress_bar_for_processor.close()
    progress_bar_for_writer.close()

    print(f"キャッシュヒット数: {sum(hit_cache_count_list)}")
