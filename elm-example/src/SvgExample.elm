module Main exposing (..)

import Svg exposing (..)
import Svg.Attributes exposing (..)


main =
    svg
        [ width "120"
        , height "120"
        , viewBox "0 0 120 120"
        ]
        [ rect
            [ x "10"
            , y "10"
            , width "100"
            , height "100"
            , rx "15"
            , ry "15"
            , fill "rgb(255, 200, 200)"
            ]
            []
        , circle
            [ cx "50"
            , cy "50"
            , r "50"
            , fill "rgb(200, 255, 200)"
            ]
            []
        ]
