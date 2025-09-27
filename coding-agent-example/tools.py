from pathlib import Path

from langchain_core.tools import tool


@tool
def read_file(filepath: str) -> str:
    """ファイルを読み込んで内容を返す。"""
    return Path(filepath).read_text(encoding="utf-8")


@tool
def write_file(filepath: str, content: str) -> None:
    """ファイルを作成して、内容を書き出す。"""
    Path(filepath).write_text(content, encoding="utf-8")


@tool
def edit_file(
    filepath: str, old_part_of_content: str, new_part_of_content: str
) -> None:
    """ファイルを作成して、内容を書き出す。"""
    file = Path(filepath)
    content = file.read_text(encoding="utf-8")
    content = content.replace(old_part_of_content, new_part_of_content)
    file.write_text(content, encoding="utf-8")
