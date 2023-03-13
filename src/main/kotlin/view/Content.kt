package view

import controller.Solver
import controller.Strategy
import csstype.AlignItems
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Board
import model.Dictionary
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Container
import mui.material.Stack
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.useEffect
import react.useState

external interface ContentProps : Props {
  var appState: AppState
  var onAppStateUpdate: (AppState) -> Unit
  var dictionary: Dictionary
  var strategy: Strategy
  var onSolve: (String) -> Unit
}

val Content = FC<ContentProps> { props ->
  val mainScope = MainScope()
  var inputLetters by useState { "" }
  var board by useState { Board() }

  useEffect {
    if (props.appState == AppState.SOLVING) {
      mainScope.launch {
        // We use delay for render cycle to update the screen
        // before we start time-consuming solve starts.
        delay(50)
        if (board.isEmpty() || props.strategy == Strategy.RandomOrder) {
          val result = Solver(props.dictionary, props.strategy).solve(inputLetters)
          if (result != null) {
            board = result
            props.onSolve(inputLetters)
          }
        }
        props.onAppStateUpdate(AppState.SHOWING_RESULT)
      }
    }
  }

  useEffect(props.dictionary) {
    board = Board()
  }

  useEffect(props.strategy) {
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
        appState = props.appState
        onReset = {
          inputLetters = ""
          board = Board()
          props.onAppStateUpdate(AppState.WAITING_FOR_INPUT)
        }
        onSubmit = {
          inputLetters = it
          props.onAppStateUpdate(AppState.SOLVING)
        }
      }

      when (props.appState) {
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
