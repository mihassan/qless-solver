package view

import kotlinx.browser.window
import mui.icons.material.DarkMode
import mui.icons.material.GitHub
import mui.icons.material.LightMode
import mui.icons.material.Menu
import mui.icons.material.QuestionMark
import mui.material.AppBar
import mui.material.IconButton
import mui.material.IconButtonColor
import mui.material.Size
import mui.material.Toolbar
import mui.material.Tooltip
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML
import react.useContext
import react.useEffect
import react.useEffectOnce
import web.cssom.Position
import web.cssom.integer
import web.cssom.number

val Header = FC<Props> {
  var theme by useContext(ThemeContext)
  var modalState by useContext(ModalStateContext)

  useEffectOnce {
    window.localStorage.getItem("theme")?.let {
      theme = when (it) {
        "light" -> Themes.Light
        "dark" -> Themes.Dark
        else -> theme
      }
    }
  }

  useEffect(theme) {
    window.localStorage.setItem("theme", theme.palette.mode.toString())
  }

  AppBar {
    sx {
      position = Position.sticky
      zIndex = integer(1_500)
    }

    Toolbar {
      IconButton {
        size = Size.large
        color = IconButtonColor.inherit
        onClick = { modalState = modalState.toggleDrawer() }
        Menu()
      }

      Typography {
        sx {
          flexGrow = number(1.0)
        }
        component = ReactHTML.h1
        variant = TypographyVariant.h6
        noWrap = true
        +"Q-Less Solver"
      }

      Tooltip {
        title = ReactNode("Theme")
        IconButton {
          color = IconButtonColor.inherit
          onClick = {
            theme = if (theme == Themes.Light) Themes.Dark else Themes.Light
          }
          if (theme == Themes.Light) DarkMode() else LightMode()
        }
      }

      Tooltip {
        title = ReactNode("View Sources")
        IconButton {
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
          color = IconButtonColor.inherit
          onClick = { modalState = modalState.openHelpDialog() }
          QuestionMark()
        }
      }
    }
  }
}
