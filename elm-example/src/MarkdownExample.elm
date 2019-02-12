module Main exposing (..)

import Markdown


main =
    Markdown.toHtml [] """
# Apple Pie Recipe

1. Invent the universe.
2. Bake an apple pie.
"""
