#!/usr/bin/env -S uv run --script
# /// script
# requires-python = ">=3.12"
# dependencies = [
#     "pyfiglet>=1.0.4",
# ]
# ///

from pyfiglet import Figlet


def main() -> None:
    text = "DEMO"
    fig = Figlet()
    ascii_art = fig.renderText(text)
    print(ascii_art)


if __name__ == "__main__":
    main()
