module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)


main =
    Browser.sandbox
        { init = init
        , view = view
        , update = update
        }


type alias Model =
    { message : String, newMessage : String }


init =
    { message = "Hello, world!", newMessage = "" }


type Msg
    = Hello
    | Submit
    | Input String


update msg model =
    case msg of
        Hello ->
            model

        Submit ->
            { message = model.newMessage, newMessage = "" }

        Input newMessage ->
            { model | newMessage = newMessage }


view : Model -> Html Msg
view model =
    div [ style "margin" "2rem" ]
        [ div [] [ text model.message ]
        , div []
            [ input [ type_ "text", value model.newMessage, onInput Input, autofocus True ] []
            , button [ onClick Submit ] [ text "Click me" ]
            ]
        ]
