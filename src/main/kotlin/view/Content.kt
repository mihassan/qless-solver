package view

import controller.Solver
import csstype.AlignItems
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.AppState
import model.Board
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
import react.useState

external interface ContentProps : Props {
  var inputLetters: String
  var onInputUpdate: (String) -> Unit
}

val Content = FC<ContentProps> { props ->
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  val configuration by useContext(ConfigurationContext)
  val dictionary by useContext(DictionaryContext)
  var solveHistory by useContext(SolveHistoryContext)
  var board by useState { Board() }

  useEffect(appState) {
    if (appState == AppState.SOLVING) {
      mainScope.launch {
        // We use delay for render cycle to update the screen
        // before we start time-consuming solve starts.
        delay(50)
        val result = Solver(dictionary, configuration.strategy).solve(props.inputLetters)
        if (result != null) {
          board = result
          solveHistory = solveHistory - props.inputLetters + props.inputLetters
        } else {
          board = Board()
        }
        appState = AppState.SHOWING_RESULT
      }
    }
  }

  useEffect(dictionary) {
    board = Board()
  }

  useEffect(configuration.strategy) {
    board = Board()
  }

  Container {
    maxWidth = "sm"

    Stack {
      sx {
        alignItems = AlignItems.center
      }

      spacing = responsive(4)

      InputForm {
        this.appState = appState
        inputLetters = props.inputLetters
        onInputUpdate = {
          props.onInputUpdate(it)
          appState = AppState.WAITING_FOR_INPUT
        }
        onSubmit = {
          appState = AppState.SOLVING
        }
      }

      when (appState) {
        AppState.SOLVING -> Alert {
          severity = AlertColor.info
          +"Solving, please wait..."
        }
        AppState.SHOWING_RESULT -> {
          if (!board.isEmpty()) {
            Grid {
              letters = board.grid()
            }
          } else {
            Alert {
              severity = AlertColor.error
              +"Sorry, no solution found"
            }
          }
        }
        else -> {}
      }
    }
  }
}
