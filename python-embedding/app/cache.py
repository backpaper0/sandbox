"""
埋め込みのキャッシュを扱うモジュール。
"""

from typing import Optional
import pickle
import hashlib

import aiosqlite


class CacheManager:
    """
    埋め込みのキャッシュを扱うクラス。
    """

    def __init__(self, cursor: aiosqlite.Cursor):
        self.cursor = cursor

    def _conevrt_id(self, text: str) -> str:
        return hashlib.md5(text.encode()).hexdigest()

    async def get(self, text: str) -> Optional[list[float]]:
        """
        指定された元テキストに対応する埋め込みのキャッシュを取得します。
        Args:
            text (str): 埋め込みの元テキスト。
        Returns:
            Optional[list[float]]: 元テキストに対応する埋め込み、または埋め込みが見つからない場合はNone。
        """
        await self.cursor.execute(
            "SELECT vector FROM vectors WHERE id = ?", (self._conevrt_id(text),)
        )
        row = await self.cursor.fetchone()
        if row is None:
            return None
        return pickle.loads(row[0])

    async def set(self, text: str, value: list[float]) -> None:
        """
        元テキストと埋め込みを受け取り、キャッシュとして保存します。
        Parameters:
            text (str): 埋め込みの元テキスト。
            value (list[float]): 元テキストに対応する埋め込み。
        Returns:
            None: 何も返しません。
        """
        await self.cursor.execute(
            "INSERT INTO vectors (id, vector) VALUES (?, ?)",
            (self._conevrt_id(text), pickle.dumps(value)),
        )
