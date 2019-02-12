module Main exposing (..)

import Browser
import Browser.Navigation as Nav
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Url exposing (..)
import Debug


main =
    Browser.application
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        , onUrlRequest = onUrlRequest
        , onUrlChange = onUrlChange
        }


type Msg
    = ChangeUrl Browser.UrlRequest
    | ChangedUrl Url


type Page
    = Foo
    | Bar
    | Baz


urlToPage url =
    case url.fragment of
        Just "foo" ->
            Foo

        Just "bar" ->
            Bar

        Just "baz" ->
            Baz

        _ ->
            Foo


type alias Model =
    { key : Nav.Key, page : Page }


init : () -> Url -> Nav.Key -> ( Model, Cmd Msg )
init () url key =
    ( Model key (urlToPage url), Cmd.none )


menu =
    ul []
        [ li [] [ a [ href "#foo" ] [ text "foo" ] ]
        , li [] [ a [ href "#bar" ] [ text "bar" ] ]
        , li [] [ a [ href "#baz" ] [ text "baz" ] ]
        ]


view : Model -> Browser.Document Msg
view model =
    case model.page of
        Foo ->
            { title = "Foo", body = [ menu, h1 [] [ text "Foo" ] ] }

        Bar ->
            { title = "Bar", body = [ menu, h1 [] [ text "Bar" ] ] }

        Baz ->
            { title = "Baz", body = [ menu, h1 [] [ text "Baz" ] ] }


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        ChangeUrl (Browser.Internal url) ->
            ( model, Nav.pushUrl model.key (Url.toString url) )

        ChangeUrl (Browser.External url) ->
            ( model, Nav.load url )

        ChangedUrl url ->
            ( { model | page = urlToPage url }, Cmd.none )


subscriptions model =
    Sub.none


onUrlRequest : Browser.UrlRequest -> Msg
onUrlRequest =
    ChangeUrl


onUrlChange : Url -> Msg
onUrlChange =
    ChangedUrl
