package view

import model.AppState
import model.Configuration
import model.Dictionary
import model.ModalState
import mui.material.CssBaseline
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import react.Context
import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.createContext
import react.useState

val ThemeContext = createStateContext(Themes.Light)

val AppStateContext = createStateContext<AppState>(AppState.PageOpened)

val ModalStateContext = createStateContext(ModalState.NONE)

val ConfigurationContext = createStateContext(Configuration.Default)

var DictionaryContext = createStateContext(Dictionary.of(""))

var SolveHistoryContext = createStateContext(emptySet<String>())

var BannedWordsContext = createStateContext(emptySet<String>())

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

private fun <T> createStateContext(initialValue: T): Context<StateInstance<T>> =
  createContext(useState { initialValue })
