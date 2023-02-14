package view

import js.core.jso
import mui.material.CssBaseline
import mui.material.PaletteMode
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.FC
import react.Props
import react.useState

enum class AppState {
  PAGE_OPENED,
  LOADING_DICTIONARY,
  WAITING_FOR_INPUT,
  SOLVING,
  SHOWING_RESULT,
}

val App = FC<Props> {
  var state by useState { AppState.PAGE_OPENED }

  ThemeProvider {
    theme = createTheme(
      jso {
        palette = jso {
          mode = PaletteMode.light

        }
      }
    )
    CssBaseline()

    Header()
    Content {
      appState = state
      onAppStateUpdate = {
        state = it
      }
    }

    Footer {
      appState = state
    }
  }
}
