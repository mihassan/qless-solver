package view

import csstype.Color
import csstype.FontSize
import csstype.TextAlign
import csstype.px
import mui.material.Box
import mui.material.Divider
import mui.material.Typography
import mui.system.sx
import react.FC
import react.Props

external interface FooterProps : Props {
  var appState: AppState
}

val Footer = FC<FooterProps> { props ->
  Box {
    Divider {}
    Typography {
      sx {
        color = Color("text.secondary")
        fontSize = FontSize.small
        padding = 16.px
        textAlign = TextAlign.end
      }
      +"Current state: ${props.appState.displayText}"
    }
  }
}

