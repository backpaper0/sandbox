"""
並列処理デモ用の遅いファイル作成スクリプト

concurrent_demo.pyでIOバウンドな処理の並列処理による性能向上をデモするため、
遅い読み込みをシミュレートする名前付きパイプ(FIFO)を作成するスクリプト。
InterpreterPoolExecutorを使用して複数のパイプを並列で作成・書き込みします。

使用例:
    python slow_files.py --size 8
"""

import os
import time
from argparse import ArgumentParser
from concurrent.futures import InterpreterPoolExecutor


def simulate_slow_file(seconds: int, id: int) -> None:
    """
    遅い書き込み処理をシミュレートする名前付きパイプを作成・操作する

    Args:
        id: ファイルの識別子(ファイル名に使用)
    """
    path = f"/tmp/slow_file_{id}"
    os.mkfifo(path)
    with open(path, mode="w") as f:
        for _ in range(seconds * 10):
            time.sleep(0.1)
            f.write(".")
            f.flush()
    time.sleep(1)
    os.remove(path)


def main(seconds: int, size: int) -> None:
    """
    並列で遅いファイル処理を実行する

    Args:
        size: 同時実行するワーカー数(作成するファイル数)
    """
    with InterpreterPoolExecutor(max_workers=size) as executor:
        futures = [
            executor.submit(simulate_slow_file, seconds, id) for id in range(size)
        ]
    for future in futures:
        future.result()


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--seconds", type=int, default=3)
    parser.add_argument("--size", type=int, required=True)
    args = parser.parse_args()
    main(args.seconds, args.size)
