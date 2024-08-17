from typing import Optional
import aiosqlite
import pickle
import hashlib


class CacheManager:
    def __init__(self, cursor: aiosqlite.Cursor):
        self.cursor = cursor

    def _conevrt_id(self, text: str) -> str:
        return hashlib.md5(text.encode()).hexdigest()

    async def get(self, text: str) -> Optional[list[float]]:
        await self.cursor.execute(
            "SELECT vector FROM vectors WHERE id = ?", (self._conevrt_id(text),)
        )
        row = await self.cursor.fetchone()
        if row is None:
            return None
        return pickle.loads(row[0])

    async def set(self, text: str, value: list[float]) -> None:
        await self.cursor.execute(
            "INSERT INTO vectors (id, vector) VALUES (?, ?)",
            (self._conevrt_id(text), pickle.dumps(value)),
        )
