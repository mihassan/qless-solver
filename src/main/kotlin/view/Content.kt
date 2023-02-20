package view

import controller.DictionaryLoader
import controller.Solver
import csstype.AlignItems
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import react.useEffectOnce
import react.useState

external interface ContentProps : Props {
  var appState: AppState
  var onAppStateUpdate: (AppState) -> Unit
}

val Content = FC<ContentProps> { props ->
  val mainScope = MainScope()
  var dictionary by useState { Dictionary.of("") }
  var inputLetters by useState { "" }
  var gridLetters: List<List<String>> by useState { emptyList() }

  useEffectOnce {
    props.onAppStateUpdate(AppState.LOADING_DICTIONARY)
    mainScope.launch {
      dictionary = DictionaryLoader.loadDictionary()
      props.onAppStateUpdate(AppState.WAITING_FOR_INPUT)
    }
  }

  useEffect {
    if (props.appState == AppState.SOLVING) {
      mainScope.launch {
        // We use delay for render cycle to update the screen
        // before we start time-consuming solve starts.
        delay(500)
        if (gridLetters.isEmpty()) {
          val result = Solver(dictionary).solve(inputLetters)
          if (result != null) {
            gridLetters = result.lines().map { it.map { "$it" } }
          }
        }
        props.onAppStateUpdate(AppState.SHOWING_RESULT)
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

      InputForm {
        appState = props.appState
        onReset = {
          inputLetters = ""
          gridLetters = emptyList()
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
          if (gridLetters.isNotEmpty()) {
            Grid {
              letters = gridLetters
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
