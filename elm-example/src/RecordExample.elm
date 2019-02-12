module Main exposing (..)

import Html.Attributes exposing (..)
import Html.Events exposing (..)


type alias Message =
    { id : Int, value : String }


xxx =
    { id = 3, value = "xxx" }


model =
    [ { id = 1, value = "aaa" }
    , Message 2 "bbb"
    , { xxx | value = "ccc" }
    ]


main =
    ul [] (model |> List.map (messageToString >> listItem))


listItem a =
    a |> text |> List.singleton |> (li [])


messageToString { id, value } =
    (String.fromInt id) ++ ": " ++ value
