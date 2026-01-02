#!/usr/bin/env -S uv run --script
#
# /// script
# requires-python = ">=3.12"
# dependencies = [
#     "pyfiglet>=1.0.4",
#     "rich>=14.2.0",
# ]
# [tool.uv]
# exclude-newer = "2026-01-01T00:00:00Z"
# ///

from typing import Tuple

from pyfiglet import Figlet
from rich.console import Console
from rich.text import Text


def lerp(a: int, b: int, t: float) -> int:
    return int(a + (b - a) * t)


def rgb_lerp(
    c1: Tuple[int, int, int], c2: Tuple[int, int, int], t: float
) -> Tuple[int, int, int]:
    return (lerp(c1[0], c2[0], t), lerp(c1[1], c2[1], t), lerp(c1[2], c2[2], t))


def hex_to_rgb(h: str) -> Tuple[int, int, int]:
    h = h.lstrip("#")
    if len(h) != 6:
        raise ValueError("HEXカラーは #RRGGBB を指定してください")
    return tuple(int(h[i : i + 2], 16) for i in (0, 2, 4))  # type: ignore


def gradient_block(
    block: str,
    start_rgb: tuple[int, int, int],
    end_rgb: tuple[int, int, int],
    per_line: bool,
    weight_style: str,
) -> Text:
    if not block:
        return Text()
    t = Text()
    if per_line:
        for line in block.splitlines(keepends=True):
            content = line[:-1] if line.endswith("\n") else line
            n = max(1, len(content) - 1)
            for i, ch in enumerate(content):
                r, g, b = rgb_lerp(start_rgb, end_rgb, i / n if n else 0)
                t.append(ch, style=f"rgb({r},{g},{b}) {weight_style}")
            if line.endswith("\n"):
                t.append("\n")
    else:
        chars = list(block)
        total = max(1, sum(1 for c in chars if c != "\n") - 1)
        seen = 0
        for ch in chars:
            if ch == "\n":
                t.append("\n")
                continue
            r, g, b = rgb_lerp(start_rgb, end_rgb, seen / total)
            t.append(ch, style=f"rgb({r},{g},{b}) {weight_style}")
            seen += 1
    return t


def main():
    console = Console()

    text = "HELLO WORLD"
    font = "Standard"
    start = hex_to_rgb("#00FFFF")
    end = hex_to_rgb("#FF00FF")

    fig = Figlet(font=font, width=120)
    ascii_art = fig.renderText(text)

    colored = gradient_block(
        ascii_art,
        start_rgb=start,
        end_rgb=end,
        per_line=True,
        weight_style="bold",
    )

    console.print(colored)


if __name__ == "__main__":
    main()
