package view

import controller.Solver
import csstype.AlignItems
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.AppState
import model.AppState.Companion.showResult
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Container
import mui.material.Stack
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.useContext
import react.useEffect
import web.navigator.navigator

val Content = FC<Props> {
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  val configuration by useContext(ConfigurationContext)
  val dictionary by useContext(DictionaryContext)
  var solveHistory by useContext(SolveHistoryContext)
  val bannedWords by useContext(BannedWordsContext)

  useEffect(appState) {
    (appState as? AppState.Solving)?.inputLetters?.let { inputLetters ->
      mainScope.launch {
        // We use delay for render cycle to update the screen
        // before we start time-consuming solve starts.
        delay(50)
        val result = Solver(dictionary, configuration.strategy, bannedWords).solve(inputLetters)
        if (result != null) {
          solveHistory = solveHistory - inputLetters + inputLetters
          console.log("Found solution:\n${result.showAsMarkDown()}\n")
          navigator.clipboard.writeText(result.show())
        }
        appState = appState.showResult(result)
      }
    }
  }

  Container {
    maxWidth = "sm"

    Stack {
      sx {
        alignItems = AlignItems.center
      }

      spacing = responsive(4)

      InputForm {}

      when (appState) {
        is AppState.Solving -> Alert {
          severity = AlertColor.info
          +"Solving, please wait..."
        }
        is AppState.ShowingResult -> {
          Grid {
            board = (appState as AppState.ShowingResult).board
          }
        }
        is AppState.NoSolutionFound -> {
          Alert {
            severity = AlertColor.error
            +"Sorry, no solution found"
          }
        }
        else -> {}
      }
    }
  }
}
