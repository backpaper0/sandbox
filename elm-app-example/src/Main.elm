module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)


---- MODEL ----


type alias Model =
    { message : String
    }


init : () -> ( Model, Cmd Msg )
init _ =
    ( Model "", Cmd.none )



---- UPDATE ----


type Msg
    = UpdateText String


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        UpdateText message ->
            ( { model | message = message }, Cmd.none )



---- VIEW ----


view : Model -> Html Msg
view { message } =
    div []
        [ h1 [] [ text message ]
        , input [ type_ "text", autofocus True, onInput UpdateText ] [ text message ]
        ]



---- PROGRAM ----


main : Program () Model Msg
main =
    Browser.element
        { view = view
        , init = init
        , update = update
        , subscriptions = always Sub.none
        }