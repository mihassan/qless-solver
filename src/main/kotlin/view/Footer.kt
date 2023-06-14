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
import react.useContext

val Footer = FC<Props> {
  val appState by useContext(AppStateContext)

  Box {
    Divider {}
    Typography {
      sx {
        color = Color("text.secondary")
        fontSize = FontSize.small
        padding = 16.px
        textAlign = TextAlign.end
      }
      +"Current state: ${appState.displayText}"
    }
  }
}
