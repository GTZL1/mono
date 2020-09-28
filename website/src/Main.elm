module Main exposing (main)

import Browser exposing (Document, UrlRequest(..))
import Browser.Navigation as Nav exposing (Key)
import Html exposing (Html, div, h1, img, li, p, span, text, ul)
import Html.Attributes exposing (class, href, src)
import Json.Decode as D
import Url exposing (Url)



--- MAIN ---


main : Program D.Value Model Msg
main =
    Browser.application
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        , onUrlRequest = onUrlRequest
        , onUrlChange = onUrlChange
        }



--- MODEL ---


type Model
    = Empty


init : D.Value -> Url -> Key -> ( Model, Cmd Msg )
init _ _ _ =
    ( Empty, Cmd.none )


onUrlRequest : UrlRequest -> Msg
onUrlRequest urlRequest =
    ClickedLink urlRequest


onUrlChange : Url -> Msg
onUrlChange url =
    ChangedUrl url



--- UPDATE ---


type Msg
    = ClickedLink UrlRequest
    | ChangedUrl Url


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case ( msg, model ) of
        ( ClickedLink urlRequest, _ ) ->
            performUrlRequest urlRequest model

        ( _, _ ) ->
            ( model, Cmd.none )


performUrlRequest : UrlRequest -> Model -> ( Model, Cmd msg )
performUrlRequest request model =
    case request of
        Internal _ ->
            ( model, Cmd.none )

        External url ->
            ( model, Nav.load url )



--- VIEW ---


view : Model -> Document Msg
view _ =
    let
        title =
            "Tupperdate.me"

        body =
            [ viewLanding
            ]
    in
    { title = title, body = body }


viewLanding : Html Msg
viewLanding =
    div
        [ class "flex flex-col"
        , class "items-stretch"
        ]
        [ tupperDate
        , addTuppFunc
        , viewTuppFunc
        , whoWeAre
        ]


tupperDate : Html Msg
tupperDate =
    div
        [ class "w-full"
        , class "h-screen lg:min-h-full"
        , class "font-archivo"
        , class "flex flex-col"
        , class "bg-white"
        ]
        [ div
            [ class "m-auto" ]
            [ h1
                [ class "w-full m-auto"
                , class "text-6xl font-bold text-center"
                , class "text-white"
                , class "customTitleGradient"
                ]
                [ text "Tupper • Date" ]
            , p
                [ class "w-full px-16 mx-auto my-8"
                , class "text-2xl text-center font-archivo"
                ]
                [ text "Discover folks who cook what you like"
                , Html.br [] []
                , text "and start sharing meals with them today."
                ]
            ]
        ]


addTuppFunc : Html Msg
addTuppFunc =
    div
        [ class "w-full overflow-hidden"
        , class "h-screen"
        , class "-mt-56"
        , class "font-archivo"
        , class "flex flex-row"
        , class "items-center justify-center"
        , class "bg-white"
        ]
        [ img
            [ src "/assets/add_chili.png"
            , class "funcImg"
            , class "-ml-20 md:m-0"
            ]
            []
        , p
            [ class "text-2xl text-center font-archivo"
            , class "m-4 md:m-8 md:ml-56"
            ]
            [ text "Add your own Tupp's"
            , Html.br [] []
            , text "and share them with the "
            , span [ class "font-bold fastTitleGradient" ] [ text "world!" ]
            ]
        ]


viewTuppFunc : Html Msg
viewTuppFunc =
    div
        [ class "w-full overflow-hidden"
        , class "h-screen"
        , class "font-archivo"
        , class "flex flex-row"
        , class "items-center justify-center"
        , class "bg-white"
        ]
        [ p
            [ class "text-2xl text-center font-archivo"
            , class "m-4 md:m-8 md:mr-56"
            ]
            [ text "When you see something "
            , span [ class "font-bold fastTitleGradient" ] [ text "yummy" ]
            , Html.br [] []
            , text "you can eat it"
            ]
        , img
            [ src "/assets/chili_view.png"
            , class "funcImg"
            , class "-mr-20 md:m-0"
            ]
            []
        ]


whoWeAre : Html Msg
whoWeAre =
    div
        [ class "w-full"
        , class "h-auto"
        , class "p-16 text-right"
        , class "text-white font-archivo"
        , class "bg-smurf-800"
        ]
        [ h1
            [ class "m-auto"
            , class "text-6xl font-bold font-archivo"
            , class "border-b-4 border-white"
            ]
            [ text "Who we are" ]
        , ul
            [ class "m-4 text-4xl font-archivo"
            ]
            [ li [] [ text "Alexandre Piveteau" ]
            , li [] [ text "Matthieu Burguburu" ]
            , li [] [ text "David Dupraz" ]
            , li [] [ text "Guy-Laurent Subri" ]
            ]
        , p
            [ class "m-4" ]
            [ text "Check us out on "
            , Html.a
                [ href "https://github.com/heig-PDG/mono"
                , class "underline"
                ]
                [ text "GitHub" ]
            ]
        ]



-- SUBSCRIPTIONS ---


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none
