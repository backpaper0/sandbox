module Main exposing (..)

import Markdown
import Browser


main =
    Browser.sandbox ({ init = init, update = update, view = view })


init =
    ()


update _ model =
    model


view _ =
    Markdown.toHtml [] """
# Apple Pie Recipe

1. Invent the universe.
2. Bake an apple pie.
"""
