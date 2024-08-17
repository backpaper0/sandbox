"""
埋め込みをインポートするためのモジュール。
"""

import json
from pathlib import Path

import aiofiles
import aiosqlite
from tqdm import tqdm

from app.cache import CacheManager


async def import_cache(
    input_file: Path,
    conn: aiosqlite.Connection,
    text_column: str,
    vector_column: str,
    show_progress_bar: bool,
) -> None:
    """
    ファイルからDBへ埋め込みをインポートします。
    Args:
        input_file (Path): 入力ファイルのパス。
        conn (aiosqlite.Connection): データベース接続オブジェクト。
        text_column (str): 元テキストのカラム名。
        vector_column (str): 埋め込みのカラム名。
        show_progress_bar (bool): 進捗バーを表示するかどうかのフラグ。
    Returns:
        None
    """

    if show_progress_bar:
        line_size = 0
        async with aiofiles.open(input_file, encoding="utf-8", mode="r") as f:
            async for _ in f:
                line_size += 1

    progress_bar = tqdm(total=line_size) if show_progress_bar else None

    total_count = 0
    import_count = 0
    async with conn.cursor() as cursor:
        cache_manager = CacheManager(cursor)
        async with aiofiles.open(input_file, encoding="utf-8", mode="r") as f:
            async for line in f:
                item = json.loads(line)
                text = str(item[text_column])
                vector_cache = await cache_manager.get(text)
                if vector_cache is None:
                    vector = list(map(float, item[vector_column]))
                    await cache_manager.set(text, vector)
                    import_count += 1
                total_count += 1
                if progress_bar is not None:
                    progress_bar.update()
        await conn.commit()
    progress_bar.close()

    print(f"総数: {total_count}")
    print(f"インポート数: {import_count}")
