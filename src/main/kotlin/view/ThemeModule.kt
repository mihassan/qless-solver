package view

import kotlinx.browser.window
import mui.material.CssBaseline
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.createContext
import react.useEffect
import react.useEffectOnce
import react.useState

typealias ThemeState = StateInstance<Theme>

val ThemeContext = createContext<ThemeState>()

val ThemeModule = FC<PropsWithChildren> { props ->
  val state = useState(Themes.Light)
  val (theme, setTheme) = state

  useEffectOnce {
    window.localStorage.getItem("theme")?.let {
      when (it) {
        "light" -> setTheme(Themes.Light)
        "dark" -> setTheme(Themes.Dark)
        else -> {}
      }
    }
  }

  useEffect(state) {
    window.localStorage.setItem("theme", theme.palette.mode.toString())
  }

  ThemeContext(state) {
    ThemeProvider {
      this.theme = theme

      CssBaseline()
      +props.children
    }
  }
}
