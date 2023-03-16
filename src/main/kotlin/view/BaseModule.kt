package view

import model.AppState
import model.Configuration
import mui.material.CssBaseline
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.createContext
import react.useState

val ThemeContext = createContext<StateInstance<Theme>>()

val AppStateContext = createContext<StateInstance<AppState>>()

val ConfigurationContext = createContext<StateInstance<Configuration>>()

var SolveHistoryContext = createContext<StateInstance<Set<String>>>()

val BaseModule = FC<PropsWithChildren> { props ->
  val themeState = useState { Themes.Light }
  val (theme) = themeState
  val appState = useState { AppState.PAGE_OPENED }
  val configuration = useState { Configuration.Default }
  val solveHistory = useState { emptySet<String>() }

  ThemeContext(themeState) {
    AppStateContext(appState) {
      ConfigurationContext(configuration) {
        SolveHistoryContext(solveHistory) {
          ThemeProvider {
            this.theme = theme

            CssBaseline()
            +props.children
          }
        }
      }
    }
  }
}
