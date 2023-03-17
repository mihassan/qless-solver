package view

import controller.Solver
import csstype.AlignItems
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.AppState
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

val Content = FC<Props> {
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  val configuration by useContext(ConfigurationContext)
  val dictionary by useContext(DictionaryContext)
  var solveHistory by useContext(SolveHistoryContext)

  useEffect(appState) {
    (appState as? AppState.Solving)?.inputLetters?.let { inputLetters ->
      mainScope.launch {
        // We use delay for render cycle to update the screen
        // before we start time-consuming solve starts.
        delay(50)
        val result = Solver(dictionary, configuration.strategy).solve(inputLetters)
        if (result != null) {
          solveHistory = solveHistory - inputLetters + inputLetters
          appState = AppState.ShowingResult(inputLetters, result)
        } else {
          appState = AppState.NoSolutionFound(inputLetters)
        }
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
            letters = (appState as AppState.ShowingResult).board.grid()
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
