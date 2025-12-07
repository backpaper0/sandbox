"""
並列処理による性能向上のデモンストレーション

IOバウンドな処理(遅いファイル読み込み)を、シングルスレッド、ThreadPoolExecutor、
InterpreterPoolExecutor、ProcessPoolExecutorの4つの実行方式で比較するスクリプト。
slow_files.pyで作成された名前付きパイプ(FIFO)を読み込み、各方式の実行時間を計測します。

使用例:
    # 事前にslow_files.pyで遅いファイルを作成
    uv run ./scripts/concurrent/slow_files.py --size 5 &
    # 各実行方式で性能を比較
    uv run ./scripts/concurrent/io_bounds_demo.py --size 5 --type single
    uv run ./scripts/concurrent/io_bounds_demo.py --size 5 --type thread
    uv run ./scripts/concurrent/io_bounds_demo.py --size 5 --type interpreter
    uv run ./scripts/concurrent/io_bounds_demo.py --size 5 --type process
"""

import logging
import time
from argparse import ArgumentParser
from concurrent.futures import (
    Future,
    InterpreterPoolExecutor,
    ProcessPoolExecutor,
    ThreadPoolExecutor,
)
from typing import Callable

logging.basicConfig(level=logging.INFO, format="%(asctime)s (%(taskName)s) %(message)s")


def read_file(id: int) -> str:
    """
    ファイルを読み込む

    Args:
        id: ファイルの識別子(slow_files.pyで作成されたファイル番号)

    Returns:
        読み込んだファイルの内容
    """
    path = f"/tmp/slow_file_{id}"
    logging.info("読み込み開始: %s", path)
    content = ""
    with open(path) as f:
        for s in f:
            content += s
    logging.info("読み込み終了: %s", path)
    return content


def main(
    size: int, executor: Callable[[Callable[[int], str], int], Future[str]]
) -> None:
    """
    指定された実行方式でファイル読み込みを実行し、経過時間を計測する

    Args:
        size: 読み込むファイル数
        executor: 実行方式を抽象化した関数(single_thread、ThreadPoolExecutor.submit等)
    """
    start = time.perf_counter()
    futures = [executor(read_file, i) for i in range(size)]
    for future in futures:
        future.result()
    end = time.perf_counter()
    elapsed = end - start
    logging.info(f"経過秒数: {elapsed:.10f} 秒")


def single_thread(f: Callable[[int], str], i: int) -> Future[str]:
    """
    シングルスレッドで関数を実行し、結果をFutureで返す

    Args:
        f: 実行する関数
        i: 関数に渡す引数

    Returns:
        実行結果を含むFuture
    """
    future = Future()
    future.set_result(f(i))
    return future


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--size", type=int, required=True)
    parser.add_argument("--type", type=str, required=True)
    args = parser.parse_args()
    size = args.size
    t = args.type
    match t:
        case "single":
            main(size, single_thread)
        case "thread":
            with ThreadPoolExecutor(max_workers=size) as executor:
                main(size, lambda f, i: executor.submit(f, i))
        case "interpreter":
            with InterpreterPoolExecutor(max_workers=size) as executor:
                main(size, lambda f, i: executor.submit(f, i))
        case "process":
            with ProcessPoolExecutor(max_workers=size) as executor:
                main(size, lambda f, i: executor.submit(f, i))
