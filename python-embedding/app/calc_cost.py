"""
埋め込みにかかるコストを計算する。
"""

import os
from pathlib import Path
from typing import Tuple

import tiktoken
from dotenv import load_dotenv
from tqdm import tqdm


def _doller_per_tokens() -> Tuple[float, int]:
    a = os.getenv("DOLLER_PER_1M_TOKENS")
    if a is not None:
        return float(a), 1_000_000
    a = os.getenv("DOLLER_PER_1K_TOKENS")
    if a is not None:
        return float(a), 1_000
    raise ValueError("DOLLER_PER_1M_TOKENS or DOLLER_PER_1K_TOKENS is not set.")


def calc_cost(
    input_file: Path,
    text_column: str,
    show_progress_bar: bool,
) -> None:
    """
    埋め込みにかかるコストを計算する。
    """
    load_dotenv()

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

    total_vector_size = 0
    with open(input_file, encoding="utf-8", mode="r") as f:
        while True:
            line = f.readline()
            if not line:
                break
            text_column = line.strip()
            vector = encoding.encode(text_column)
            total_vector_size += len(vector)
            if progress_bar is not None:
                progress_bar.update()

    print(f"ベクトルの総サイズ: {total_vector_size:,}")
    doller = total_vector_size / div * dpt
    print(f"コスト: {doller * float(ypd):,}円 ({doller:,}ドル)")
