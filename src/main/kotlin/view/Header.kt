package view

import csstype.Position
import csstype.number
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.material.AppBar
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Size
import mui.material.Toolbar
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML
import react.useContext

val Header = FC<Props> {
  var theme by useContext(ThemeContext)

  AppBar {
    sx {
      position = Position.sticky
    }

    Toolbar {
      Typography {
        sx {
          flexGrow = number(1.0)
        }
        component = ReactHTML.h1
        variant = TypographyVariant.h6
        noWrap = true
        +"Q-Less Solver"
      }

      Button {
        variant = ButtonVariant.contained
        size = Size.small
        onClick = {
          theme = if (theme == Themes.Light) Themes.Dark else Themes.Light
        }
        when (theme) {
          Themes.Light -> {
            Brightness4.create()
            +"Light Mode"
          }
          Themes.Dark -> {
            Brightness7.create()
            +"Dark Mode"
          }
        }
      }
    }
  }
}
