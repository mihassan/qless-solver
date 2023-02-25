package view

import csstype.Position
import csstype.number
import csstype.integer
import kotlinx.browser.window
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.icons.material.GitHub
import mui.icons.material.Menu
import mui.icons.material.QuestionMark
import mui.material.AppBar
import mui.material.IconButton
import mui.material.IconButtonColor
import mui.material.Size
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
import react.useState

val Header = FC<Props> {
  var theme by useContext(ThemeContext)
  var showHelpDialog by useState { false }
  var showDrawer by useState { false }

  AppBar {
    sx {
      position = Position.sticky
      zIndex = integer(1_500)
    }

    Toolbar {
      IconButton {
        size = Size.large
        color = IconButtonColor.inherit
        onClick = {
          showDrawer = !showDrawer
        }
        Menu()
      }

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

      Tooltip {
        title = ReactNode("View Sources")
        IconButton {
          size = Size.large
          color = IconButtonColor.inherit
          onClick = {
            window.location.href = "https://github.com/mihassan/qless-solver/"
          }
          GitHub()
        }
      }

      Tooltip {
        title = ReactNode("How to Use")
        IconButton {
          onClick = { showHelpDialog = true }
          size = Size.large
          color = IconButtonColor.inherit
          QuestionMark()
        }
      }
    }
  }

  HelpDialog {
    isOpen = showHelpDialog
    onClose = { showHelpDialog = false }
  }

  Drawer {
    isOpen = showDrawer
    onClose = { showDrawer = false }
  }
}
