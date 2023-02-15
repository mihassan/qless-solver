package view

import csstype.Position
import mui.material.AppBar
import mui.material.Toolbar
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML

val Header = FC<Props> {
  AppBar {
    sx {
      position = Position.sticky
    }
    Toolbar {
      Typography {
        variant = TypographyVariant.h5
        noWrap = true
        component = ReactHTML.div
        +"Q-Less Solver"
      }
    }
  }
}
