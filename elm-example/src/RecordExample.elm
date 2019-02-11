module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)


type alias Message =
    { id : Int, value : String }


main =
    Browser.sandbox
        { init = init
        , update = update
        , view = view
        }


xxx =
    { id = 3, value = "xxx" }


init =
    [ { id = 1, value = "aaa" }
    , Message 2 "bbb"
    , { xxx | value = "ccc" }
    ]


update () model =
    model


view model =
    ul [] (model |> List.map (messageToString >> listItem))


listItem a =
    a |> text |> List.singleton |> (li [])


messageToString { id, value } =
    (String.fromInt id) ++ ": " ++ value
