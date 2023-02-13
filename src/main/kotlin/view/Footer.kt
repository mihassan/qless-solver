package view

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface FooterProps : Props {
  var appState: AppState
}

val Footer = FC<FooterProps> { props ->
  div {
    css {
      padding = 10.px
      position = Position.absolute
      boxSizing = BoxSizing.borderBox
      bottom = 0.px
      width = 100.pct
      fontSize = FontSize.small
      backgroundColor = NamedColor.lightgray
      textAlign = TextAlign.end
    }
    +"Current state: ${props.appState}"
  }
}
