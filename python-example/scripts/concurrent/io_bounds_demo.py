"""
並列処理による性能向上のデモンストレーション

IOバウンドな処理(遅いHTTPリクエスト)を、シングルスレッド、ThreadPoolExecutor、
InterpreterPoolExecutor、ProcessPoolExecutorの4つの実行方式で比較するスクリプト。
HTTPBinを使用して3秒遅延するリクエストを実行し、各方式の実行時間を計測します。

使用例:
    # 事前にHTTPBinをDockerで起動
    docker run -p 8080:80 kennethreitz/httpbin
    # 各実行方式で性能を比較
    uv run ./scripts/concurrent/io_bounds_demo.py --size 4 --type single
    uv run ./scripts/concurrent/io_bounds_demo.py --size 4 --type thread
    uv run ./scripts/concurrent/io_bounds_demo.py --size 4 --type interpreter
    uv run ./scripts/concurrent/io_bounds_demo.py --size 4 --type process
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
from urllib import request

logging.basicConfig(level=logging.INFO, format="%(asctime)s (%(taskName)s) %(message)s")


def do_http_request(id: int) -> str:
    """
    HTTPBinに対して遅延リクエストを実行する

    Args:
        id: リクエストの識別子(並列実行時の区別用)

    Returns:
        HTTPレスポンスの内容
    """
    logging.info("読み込み開始")
    with request.urlopen("http://localhost:8080/delay/3") as response:
        content = response.read().decode("utf-8")
    logging.info("読み込み終了")
    return content


def main(
    size: int, executor: Callable[[Callable[[int], str], int], Future[str]]
) -> None:
    """
    指定された実行方式でHTTPリクエストを実行し、経過時間を計測する

    Args:
        size: 実行するリクエスト数
        executor: 実行方式を抽象化した関数(single_thread、ThreadPoolExecutor.submit等)
    """
    start = time.perf_counter()
    futures = [executor(do_http_request, i) for i in range(size)]
    for future in futures:
        future.result()
    end = time.perf_counter()
    elapsed = end - start
    logging.info("経過秒数: %.3f 秒", elapsed)


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
