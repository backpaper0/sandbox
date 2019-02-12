module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Json.Decode as Decode


jsonString =
    """
{
    "id": 12345,
    "name": "example",
    "roles": [
        {"name":"USER"},
        {"name":"ADMIN"}
    ]
}
"""


type alias Role =
    { name : String }


type alias User =
    { id : Int, name : String, roles : List Role }


idDecoder =
    Decode.field "id" Decode.int


nameDecoder =
    Decode.field "name" Decode.string


roleDecoder =
    Decode.map Role nameDecoder


rolesDecoder =
    Decode.field "roles" (Decode.list roleDecoder)


userDecoder =
    Decode.map3 User
        idDecoder
        nameDecoder
        rolesDecoder


model =
    Decode.decodeString userDecoder jsonString


main =
    case model of
        Ok user ->
            div []
                [ p [] [ text ((String.fromInt user.id) ++ ": " ++ user.name) ]
                , ul [] (user.roles |> List.map role2li)
                ]

        Err _ ->
            p [] [ text "error" ]


role2li =
    .name >> text >> List.singleton >> (li [])
