from dotenv import load_dotenv
from openai import AsyncOpenAI
from openai.types import CreateEmbeddingResponse
from pathlib import Path
from tqdm.asyncio import tqdm
import asyncio
import json
import os
import pandas as pd

async def proc(client: AsyncOpenAI, semaphore: asyncio.Semaphore, line: str) -> CreateEmbeddingResponse:
    async with semaphore:
        resp = await client.embeddings.create(
            input=line,
            model=os.environ["EMBEDDING_MODEL"],
        )
        return resp

async def main() -> None:
    load_dotenv()

    client = AsyncOpenAI()

    with open(Path("data") / "gitlog.txt") as file:
        all_lines = file.readlines()

    tasks: list[asyncio.Task] = []
    semaphore = asyncio.Semaphore(20)
    for line in all_lines:
        task = asyncio.create_task(proc(client, semaphore, line))
        tasks.append(task)

    resps: list[CreateEmbeddingResponse] = await tqdm.gather(*tasks)

    records = [
        {
            "line": line,
            "embedding": json.dumps(resp.data[0].embedding),
        }
        for line, resp in zip(all_lines, resps)
    ]

    df = pd.DataFrame(columns=["line", "embedding"], data=records)

    output = Path("data") / "output.jsonl"
    df.to_json(output, orient="records", lines=True, force_ascii=False)

if __name__ == "__main__":
    asyncio.run(main())
