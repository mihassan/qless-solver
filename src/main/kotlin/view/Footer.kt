package view

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface FooterProps : Props {
  var appState: AppState
}

val Footer = FC<FooterProps> { props ->
  ReactHTML.div {
    css {
      position = Position.absolute
      bottom = 10.px
      fontSize = FontSize.small
    }
    +"Current state: ${props.appState}"
  }
}