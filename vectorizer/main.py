import asyncio
import json
from typing import Any
from langchain_openai import OpenAIEmbeddings
from settings import settings
from pydantic import SecretStr
import aiofiles
from tqdm.asyncio import tqdm
from langchain.embeddings import CacheBackedEmbeddings
from langchain_core.embeddings import Embeddings

from valkey_store import ValkeyStore
from redis.asyncio import Redis


async def vectorize(
    *,
    progress_bar: tqdm,
    in_queue: asyncio.Queue[dict[str, Any] | None],
    out_queue: asyncio.Queue[dict[str, Any] | None],
    embeddings: Embeddings,
):
    while True:
        doc = await in_queue.get()
        if doc is None:
            break
        kvs = [(k, v) for k, v in settings.targets.items()]
        texts = [doc[key] for key, _ in kvs]
        embedding = await embeddings.aembed_documents(texts)
        for (_, value), emb in zip(kvs, embedding):
            doc[value] = emb
        await out_queue.put(doc)
        in_queue.task_done()
        progress_bar.update(1)


async def read_file(*, progress_bar: tqdm, queue: asyncio.Queue[dict[str, Any] | None]):
    async with aiofiles.open(settings.src_filepath, mode="r", encoding="utf-8") as f:
        async for line in f:
            line = line.strip()
            if not line:
                break
            doc = json.loads(line)
            await queue.put(doc)
            progress_bar.update(1)


async def write_file(
    *, progress_bar: tqdm, queue: asyncio.Queue[dict[str, Any] | None]
):
    async with aiofiles.open(settings.dest_filepath, mode="w", encoding="utf-8") as f:
        while True:
            doc = await queue.get()
            if doc is None:
                break
            await f.write(json.dumps(doc, ensure_ascii=False) + "\n")
            queue.task_done()
            progress_bar.update(1)


async def run(embeddings: Embeddings) -> None:
    in_queue = asyncio.Queue(settings.parallels)
    out_queue = asyncio.Queue()
    reader_progress_bar = tqdm(total=settings.total, position=0, desc="Read")
    processor_progress_bar = tqdm(total=settings.total, position=1, desc="Process")
    writer_progress_bar = tqdm(total=settings.total, position=2, desc="Write")

    reader_task = asyncio.create_task(
        read_file(progress_bar=reader_progress_bar, queue=in_queue)
    )
    processor_tasks = [
        asyncio.create_task(
            vectorize(
                progress_bar=processor_progress_bar,
                in_queue=in_queue,
                out_queue=out_queue,
                embeddings=embeddings,
            )
        )
        for _ in range(settings.parallels)
    ]
    writer_task = asyncio.create_task(
        write_file(progress_bar=writer_progress_bar, queue=out_queue)
    )

    await reader_task
    for _ in range(settings.parallels):
        await in_queue.put(None)
    await asyncio.gather(*processor_tasks)
    await out_queue.put(None)
    await writer_task


async def main():
    embeddings = OpenAIEmbeddings(
        api_key=SecretStr(settings.openai_api_key),
        model=settings.embedding_model,
        dimensions=settings.embedding_dimensions,
    )
    if settings.valkey_url is None:
        await run(embeddings)
    else:
        async with Redis.from_url(settings.valkey_url) as redis:
            document_embedding_store = ValkeyStore(redis)
            embeddings = CacheBackedEmbeddings(embeddings, document_embedding_store)
            await run(embeddings)


if __name__ == "__main__":
    asyncio.run(main())
