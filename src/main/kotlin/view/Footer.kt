package view

import csstype.Color
import csstype.TextAlign
import csstype.px
import mui.material.Box
import mui.material.Divider
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props

external interface FooterProps : Props {
  var appState: AppState
}

val Footer = FC<FooterProps> { props ->
  Box {
    Divider {}
    sx {
      padding = 16.px
      textAlign = TextAlign.end
    }
    Typography {
      sx {
        color = Color("text.secondary")
      }
      variant = TypographyVariant.caption
      +"Current state: ${props.appState}"
    }
  }
}

