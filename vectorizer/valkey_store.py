import asyncio
from typing import Awaitable, Coroutine, Iterator, Sequence, cast
from langchain_core.stores import BaseStore
from redis.asyncio import Redis
import pickle


class ValkeyStore(BaseStore[str, list[float]]):
    _client: Redis

    def __init__(self, client: Redis) -> None:
        self._client = client

    def mget(self, keys: Sequence[str]) -> list[list[float] | None]:
        return asyncio.run(self.amget(keys))

    async def amget(self, keys: Sequence[str]) -> list[list[float] | None]:
        embeddings: list[list[float] | None] = []
        for key in keys:
            response = await cast(Awaitable[bytes | None], self._client.get(key))
            if response is None:
                embeddings.append(None)
            else:
                embeddings.append(cast(list[float], pickle.loads(response)))
        return embeddings

    def mset(self, key_value_pairs: Sequence[tuple[str, list[float]]]) -> None:
        asyncio.run(self.amset(key_value_pairs))

    async def amset(self, key_value_pairs: Sequence[tuple[str, list[float]]]) -> None:
        tasks = [
            asyncio.create_task(
                cast(Coroutine, self._client.set(key, pickle.dumps(value)))
            )
            for key, value in key_value_pairs
        ]
        await asyncio.gather(*tasks)

    def mdelete(self, keys: Sequence[str]) -> None:
        asyncio.run(self.amdelete(keys))

    async def amdelete(self, keys: Sequence[str]) -> None:
        tasks = [
            asyncio.create_task(cast(Coroutine, self._client.delete(key)))
            for key in keys
        ]
        await asyncio.gather(*tasks)

    def yield_keys(self, *, prefix: str | None = None) -> Iterator[str]:
        return iter(
            asyncio.run(
                cast(
                    Coroutine[None, None, list[str]],
                    self._client.keys(pattern=f"{prefix}*"),
                )
            )
        )
