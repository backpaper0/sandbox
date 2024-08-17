"""
埋め込みを扱う簡易ツール。
"""

import argparse
import asyncio
import os
from pathlib import Path

import aiosqlite
from dotenv import load_dotenv

from app.calc_cost import calc_cost
from app.import_cache import import_cache
from app.embeddings import embeddings


async def main() -> None:
    """
    メイン関数です。
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("-c", "--cost", help="コストを計算する", action="store_true")
    parser.add_argument(
        "-I",
        "--import",
        dest="is_import",
        help="データベースへ埋め込みをインポートする",
        action="store_true",
    )
    parser.add_argument(
        "-i",
        "--input",
        dest="input_path",
        help="入力ファイル",
        type=Path,
        required=True,
    )
    parser.add_argument(
        "-o", "--output", dest="output_path", help="出力ファイル", type=Path
    )
    parser.add_argument(
        "-t",
        "--text-column",
        help="元テキストフィールドの名前",
        type=str,
        default="text",
    )
    parser.add_argument(
        "-v",
        "--vector-column",
        help="埋め込みフィールドの名前",
        type=str,
        default="vector",
    )
    parser.add_argument("-p", "--parallels", help="並列度", type=int, default=10)
    parser.add_argument("-P", "--progress", help="進捗の表示", action="store_false")
    args = parser.parse_args()

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
                input_file=args.input_path,
                text_column=args.text_column,
                show_progress_bar=args.progress,
                conn=conn,
            )
        elif args.is_import:
            await import_cache(
                input_file=args.input_path,
                conn=conn,
                text_column=args.text_column,
                vector_column=args.vector_column,
                show_progress_bar=args.progress,
            )
        else:
            await embeddings(
                input_file=args.input_path,
                output_file=args.output_path,
                parallels=args.parallels,
                show_progress_bar=args.progress,
                conn=conn,
                text_column=args.text_column,
                vector_column=args.vector_column,
            )


if __name__ == "__main__":
    asyncio.run(main())
