package view

import csstype.AlignSelf
import csstype.Color
import csstype.Position
import csstype.rem
import mui.icons.material.OpenInNew
import mui.material.AppBar
import mui.material.Link
import mui.material.Toolbar
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import web.window.WindowTarget

val Header = FC<Props> {
  AppBar {
    sx {
      position = Position.sticky
    }
    Toolbar {
      sx {
        alignSelf = AlignSelf.center
      }
      Typography {
        component = ReactHTML.h1
        variant = TypographyVariant.h6
        noWrap = true
        +"Q-Less Solver"
      }
      Link {
        href = "https://q-lessgame.com/"
        target = WindowTarget._blank
        OpenInNew {
          sx {
            color = Color("primary.contrastText")
            fontSize = 1.5.rem
            alignSelf = AlignSelf.normal
          }
        }
      }
    }
  }
}
