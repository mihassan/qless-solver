package view

import csstype.AlignSelf
import csstype.Color
import csstype.Position
import mui.icons.material.OpenInNew
import mui.material.AppBar
import mui.material.Link
import mui.material.SvgIconSize
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
        variant = TypographyVariant.h6
        noWrap = true
        component = ReactHTML.div
        +"Q-Less Solver"
      }
      Link {
        href = "https://q-lessgame.com/"
        target = WindowTarget._blank
        OpenInNew {
          fontSize = SvgIconSize.small
          sx {
            color = Color("primary.contrastText")
          }
        }
      }
    }
  }
}
