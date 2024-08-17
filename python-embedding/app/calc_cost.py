"""
埋め込みにかかるコストを計算する。
"""

import json
import os
from pathlib import Path
from typing import Tuple

import tiktoken
from tqdm import tqdm
import aiosqlite
from app.cache import CacheManager


def _doller_per_tokens() -> Tuple[float, int]:
    dpt = os.getenv("DOLLER_PER_1M_TOKENS")
    if dpt is not None:
        return float(dpt), 1_000_000
    dpt = os.getenv("DOLLER_PER_1K_TOKENS")
    if dpt is not None:
        return float(dpt), 1_000
    raise ValueError("DOLLER_PER_1M_TOKENS or DOLLER_PER_1K_TOKENS is not set.")


async def calc_cost(
    input_file: Path,
    text_column: str,
    show_progress_bar: bool,
    conn: aiosqlite.Connection,
) -> None:
    """
    埋め込みにかかるコストを計算する。
    """

    async with conn.cursor() as cursor:
        cache = CacheManager(cursor)

        model_name = os.environ["EMBEDDINGS_MODEL"]
        dpt, div = _doller_per_tokens()
        ypd = os.environ["YEN_PER_DOLLER"]

        encoding = tiktoken.encoding_for_model(model_name)

        line_size = 0
        if show_progress_bar:
            with open(input_file, encoding="utf-8", mode="r") as f:
                while True:
                    line = f.readline()
                    if not line:
                        break
                    line_size += 1

        progress_bar = tqdm(total=line_size) if show_progress_bar else None

        total_tokens_size = 0
        hit_cache_count = 0
        with open(input_file, encoding="utf-8", mode="r") as f:
            while True:
                line = f.readline()
                if not line:
                    break
                item = json.loads(line)
                text = str(item[text_column])
                vector = await cache.get(text)
                if vector is not None:
                    hit_cache_count += 1
                else:
                    tokens = encoding.encode(text)
                    total_tokens_size += len(tokens)
                if progress_bar is not None:
                    progress_bar.update()

    print(f"キャッシュヒット数: {hit_cache_count}")
    print(f"トークンの総サイズ: {total_tokens_size:,}")
    doller = total_tokens_size / div * dpt
    print(f"コスト: {doller * float(ypd):,}円 ({doller:,}ドル)")
