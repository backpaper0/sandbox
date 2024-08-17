import json
import os
from pathlib import Path
from typing import Optional
import aiosqlite
import asyncio
import aiofiles
from openai import AsyncOpenAI
from tqdm import tqdm
import hashlib

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
    client: AsyncOpenAI,
    input_queue: asyncio.Queue,
    output_queue: asyncio.Queue,
    text_column: str,
    vector_column: str,
    conn: aiosqlite.Connection,
    embeddings_model: str,
    progress_bar: Optional[tqdm] = None,
) -> None:
    while True:
        line = await input_queue.get()
        if line is None:
            input_queue.task_done()
            break
        item = json.loads(line)
        text = item[text_column]
        resp = await client.embeddings.create(input=text, model=embeddings_model)
        vector = resp.data[0].embedding
        item[vector_column] = vector
        line = json.dumps(item, ensure_ascii=False)
        await output_queue.put(line)
        input_queue.task_done()
        if progress_bar is not None:
            progress_bar.update()


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
    if show_progress_bar:
        total = sum(1 for _ in open(input_file, encoding="utf-8", mode="r"))
        progress_bar_for_reader = tqdm(total=total)
        progress_bar_for_processor = tqdm(total=total)
        progress_bar_for_writer = tqdm(total=total)
    else:
        progress_bar_for_reader = None
        progress_bar_for_processor = None
        progress_bar_for_writer = None

    input_queue: asyncio.Queue = asyncio.Queue(parallels)
    output_queue: asyncio.Queue = asyncio.Queue(parallels)

    client = AsyncOpenAI()
    embeddings_model = os.environ["EMBEDDINGS_MODEL"]

    reader_task = asyncio.create_task(
        _reader(input_file, input_queue, progress_bar_for_reader)
    )
    processor_task = asyncio.gather(
        *[
            _processor(
                client=client,
                input_queue=input_queue,
                output_queue=output_queue,
                text_column=text_column,
                vector_column=vector_column,
                conn=conn,
                progress_bar=progress_bar_for_processor,
                embeddings_model=embeddings_model,
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
    await processor_task
    await output_queue.put(None)
    await writer_task

    progress_bar_for_reader.close()
    progress_bar_for_processor.close()
    progress_bar_for_writer.close()
