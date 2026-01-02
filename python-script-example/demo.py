#!/usr/bin/env -S uv run --script
# /// script
# requires-python = ">=3.14"
# dependencies = [
#     "pyfiglet>=1.0.4",
# ]
# [tool.uv]
# exclude-newer = "2026-01-01T00:00:00Z"
# ///

from pyfiglet import Figlet


def main() -> None:
    text = "DEMO"
    fig = Figlet()
    ascii_art = fig.renderText(text)
    print(ascii_art)


if __name__ == "__main__":
    main()
