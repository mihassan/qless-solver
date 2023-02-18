package view

import csstype.Auto
import csstype.Display
import csstype.array
import csstype.dvh
import csstype.fr
import mui.material.Box
import mui.material.CssBaseline
import mui.material.styles.ThemeProvider
import mui.system.sx
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
    theme = Themes.Light

    CssBaseline()

    Box {
      sx {
        display = Display.grid
        gridTemplateRows = array(Auto.auto, 1.fr, Auto.auto)
        height = 100.dvh
      }

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
}
