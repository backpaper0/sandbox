"""
CPUバウンドな処理に対する並列化の効果を検証するデモプログラム

素数判定という計算集約的な処理を用いて、以下の4つの実行方式を比較する:
1. single: シングルスレッド実行(ベースライン)
2. thread: ThreadPoolExecutor(GILの影響でCPUバウンド処理では効果が薄い)
3. interpreter: InterpreterPoolExecutor(サブインタプリタによる並列実行)
4. process: ProcessPoolExecutor(マルチプロセスによる真の並列実行)

使用例:
    uv run ./scripts/concurrent/cpu_bounds_demo.py --size 4 --type single
    uv run ./scripts/concurrent/cpu_bounds_demo.py --size 4 --type thread
    uv run ./scripts/concurrent/cpu_bounds_demo.py --size 4 --type interpreter
    uv run ./scripts/concurrent/cpu_bounds_demo.py --size 4 --type process
"""

import logging
import math
import time
from argparse import ArgumentParser
from concurrent.futures import (
    Future,
    InterpreterPoolExecutor,
    ProcessPoolExecutor,
    ThreadPoolExecutor,
)
from typing import Callable

logging.basicConfig(
    level=logging.INFO, format="%(asctime)s (%(threadName)s) %(process)d - %(message)s"
)


def is_prime(n: int) -> bool:
    """
    素数判定を行う関数(CPUバウンドな処理)

    試し割り法を使用して素数判定を行う。
    計算量がO(√n)のため、大きな数に対しては計算コストが高い。

    Args:
        n: 判定対象の整数

    Returns:
        nが素数ならTrue、そうでなければFalse
    """
    if n < 2:
        return False
    if n % 2 == 0:
        return n == 2
    limit = int(math.sqrt(n)) + 1
    for i in range(3, limit, 2):
        if n % i == 0:
            return False
    return True


def count_primes_range(args: tuple[int, int]) -> int:
    """
    指定範囲内の素数をカウントする

    指定された範囲[start, end)内の素数の個数を数える。
    各範囲は独立して処理できるため、並列化に適している。

    Args:
        args: (start, end)のタプル。範囲は[start, end)

    Returns:
        範囲内の素数の個数
    """
    start, end = args
    logging.info("count_primes_range開始: start=%s, end=%s", start, end)
    start_time = time.perf_counter()
    count = 0
    for n in range(start, end):
        if is_prime(n):
            count += 1
    end_time = time.perf_counter()
    elapsed = end_time - start_time
    logging.info(
        "count_primes_range終了: start=%s, end=%s (%.10f 秒)", start, end, elapsed
    )
    return count


def main(
    limit: int,
    size: int,
    executor: Callable[
        [Callable[[tuple[int, int]], int], tuple[int, int]], Future[int]
    ],
) -> None:
    """
    指定された実行方式で素数カウントを実行し、経過時間を計測する

    0からlimitまでの範囲をsize個のチャンクに分割し、
    各チャンクを並列(または逐次)に処理して素数の総数を求める。

    Args:
        limit: 素数判定を行う上限値(0からlimit未満の範囲)
        size: 処理を分割する数(ワーカー数)
        executor: 実行方式を抽象化した関数(single_thread、ThreadPoolExecutor.submit等)
    """
    start_time = time.perf_counter()

    # 処理範囲を均等に分割
    chunk = limit // size
    ranges = []
    start = 0
    for i in range(size):
        end = start + chunk if i < size - 1 else limit
        ranges.append((start, end))
        start = end

    # 各範囲で素数カウントを実行
    futures = [executor(count_primes_range, range) for range in ranges]
    # 全ての結果を集計
    prime_count = sum(future.result() for future in futures)
    end_time = time.perf_counter()
    elapsed = end_time - start_time
    logging.info("%d未満の素数の個数: %d", limit, prime_count)
    logging.info("経過秒数: %.10f 秒", elapsed)


def single_thread(
    f: Callable[[tuple[int, int]], int], i: tuple[int, int]
) -> Future[int]:
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
    parser.add_argument(
        "--size", type=int, required=True, help="ワーカー数(処理分割数)"
    )
    parser.add_argument(
        "--type",
        type=str,
        required=True,
        choices=["single", "thread", "interpreter", "process"],
        help="実行タイプ: single(逐次), thread(スレッド並列), interpreter(サブインタプリタ), process(マルチプロセス)",
    )
    parser.add_argument(
        "--limit",
        type=int,
        default=10_000_000,
        help="素数判定の上限値(デフォルト: 10,000,000)",
    )
    args = parser.parse_args()
    limit = args.limit
    size = args.size
    t = args.type
    match t:
        case "single":
            # シングルスレッド実行(ベースライン)
            main(limit, size, single_thread)
        case "thread":
            # ThreadPoolExecutor: GILの影響でCPUバウンドでは効果が薄い
            with ThreadPoolExecutor(max_workers=size) as executor:
                main(limit, size, lambda f, i: executor.submit(f, i))
        case "interpreter":
            # InterpreterPoolExecutor: サブインタプリタによる並列実行(Python 3.13+)
            with InterpreterPoolExecutor(max_workers=size) as executor:
                main(limit, size, lambda f, i: executor.submit(f, i))
        case "process":
            # ProcessPoolExecutor: 真のマルチプロセス並列実行でCPUバウンドに効果的
            with ProcessPoolExecutor(max_workers=size) as executor:
                main(limit, size, lambda f, i: executor.submit(f, i))
