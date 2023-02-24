package view

import csstype.Position
import csstype.number
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.material.AppBar
import mui.material.Switch
import mui.material.Toolbar
import mui.material.Tooltip
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
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
        variant = TypographyVariant.h4
        noWrap = true
        +"Q-Less Solver"
      }

      Tooltip {
        title = ReactNode("Theme")
        Switch {
          icon = Brightness7.create()
          checkedIcon = Brightness4.create()
          checked = theme == Themes.Dark

          onChange = { _, checked ->
            theme = if (checked) Themes.Dark else Themes.Light
          }
        }
      }
    }
  }
}
