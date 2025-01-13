"""
bisect.bisect_leftを使って既定のトークン数以下になるようにテキストを切って返す。

poetry run python -m bisect_example.main
"""

from bisect import bisect_left
from pathlib import Path
from tiktoken import Encoding, encoding_for_model


class TextForBisect:
    _encoding: Encoding
    _text: str

    def __init__(self, encoding: Encoding, text: str) -> None:
        self._encoding = encoding
        self._text = text

    def __len__(self) -> int:
        return len(self._text)

    def __getitem__(self, index: int) -> int:
        return len(self._encoding.encode(self._text[: index + 1]))

    def sub_text(self, token_lengh: int) -> str:
        return self._text[: bisect_left(self, token_lengh) + 1]


def main() -> None:
    encoding = encoding_for_model("gpt-4o")
    text = (Path(__file__).parent / "rashomon.txt").read_text(encoding="utf-8")
    text_for_bisect = TextForBisect(encoding, text)

    print(f"text length: {len(text)}")
    print(f"token length: {len(encoding.encode(text))}")
    print()

    def proc(token_length: int) -> None:
        print("-" * 100)
        print(f"parameter = {token_length}")
        sub_text = text_for_bisect.sub_text(token_length)
        print(f"  text: {len(sub_text)}")
        print(f"  token: {len(encoding.encode(sub_text))}")
        print(sub_text)

    proc(0)

    proc(100)

    proc(465)

    proc(466)

    proc(467)


if __name__ == "__main__":
    main()
