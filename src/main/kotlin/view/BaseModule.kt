package view

import model.AppState
import model.Configuration
import model.Dictionary
import model.ModalState
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

val ModalStateContext = createContext<StateInstance<ModalState>>()

val ConfigurationContext = createContext<StateInstance<Configuration>>()

var DictionaryContext = createContext<StateInstance<Dictionary>>()

var SolveHistoryContext = createContext<StateInstance<Set<String>>>()

var BannedWordsContext = createContext<StateInstance<Set<String>>>()

val BaseModule = FC<PropsWithChildren> { props ->
  val themeState: StateInstance<Theme> = useState { Themes.Light }
  val (theme: Theme) = themeState
  val appState: StateInstance<AppState> = useState { AppState.PageOpened }
  val modalState: StateInstance<ModalState> = useState { ModalState.NONE }
  val configuration: StateInstance<Configuration> = useState { Configuration.Default }
  val dictionary: StateInstance<Dictionary> = useState { Dictionary.of("") }
  val solveHistory: StateInstance<Set<String>> = useState { emptySet() }
  val bannedWords: StateInstance<Set<String>> = useState { emptySet() }

  ThemeContext(themeState) {
    AppStateContext(appState) {
      ModalStateContext(modalState) {
        ConfigurationContext(configuration) {
          DictionaryContext(dictionary) {
            SolveHistoryContext(solveHistory) {
              BannedWordsContext(bannedWords) {
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
    }
  }
}
