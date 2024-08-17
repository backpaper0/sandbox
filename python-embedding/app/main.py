"""
埋め込みを扱う簡易ツール。
"""

import argparse
from pathlib import Path
from app.calc_cost import calc_cost
from app.embeddings import embeddings
import asyncio
import aiosqlite
import os
from dotenv import load_dotenv


async def main(args: argparse.Namespace) -> None:
    load_dotenv()
    async with aiosqlite.connect(os.environ["DB_PATH"]) as conn:
        async with conn.cursor() as cursor:
            await cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS vectors (
                    id TEXT PRIMARY KEY,
                    vector BLOB NOT NULL
                )
                """
            )
            await conn.commit()

        if args.cost:
            await calc_cost(
                input_file=args.input,
                text_column=args.text,
                show_progress_bar=args.progress,
                conn=conn,
            )
        elif args.import_:
            print("Importing vectors...")
        else:
            await embeddings(
                input_file=args.input,
                output_file=args.output,
                parallels=args.parallels,
                show_progress_bar=args.progress,
                conn=conn,
                text_column=args.text,
                vector_column=args.vector,
            )


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

    asyncio.run(main(args=args))
