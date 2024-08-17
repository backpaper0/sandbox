"""
埋め込みを扱う簡易ツール。
"""

import argparse
from pathlib import Path
from app.calc_cost import calc_cost


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-c", "--cost", help="コストを計算する", action="store_true")
    parser.add_argument(
        "-I",
        "--import",
        dest="import_",
        help="データベースへベクトル値をインポートする",
        action="store_true",
    )
    parser.add_argument("-i", "--input", help="入力ファイル", type=Path, required=True)
    parser.add_argument("-o", "--output", help="出力ファイル", type=Path)
    parser.add_argument(
        "-t", "--text", help="テキストフィールドの名前", type=str, default="text"
    )
    parser.add_argument(
        "-v", "--vector", help="ベクトルフィールドの名前", type=str, default="vector"
    )
    parser.add_argument("-p", "--parallels", help="並列度", type=int, default=10)
    parser.add_argument("-P", "--progress", help="進捗の表示", action="store_false")
    args = parser.parse_args()

    if args.cost:
        calc_cost(
            input_file=args.input,
            text_column=args.text,
            show_progress_bar=args.progress,
        )
    elif args.import_:
        print("Importing vectors...")
    else:
        print("Main action")
