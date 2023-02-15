package view

import controller.DictionaryLoader
import controller.Solver
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Dictionary
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.p
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
        delay(100)
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

  Box {
    sx {
      padding = 24.px
    }
    when (props.appState) {
      AppState.PAGE_OPENED -> p { +"Welcome" }
      AppState.LOADING_DICTIONARY -> p { +"Loading dictionary..." }
      AppState.WAITING_FOR_INPUT -> {
        InputForm {
          onSubmit = {
            inputLetters = it
            props.onAppStateUpdate(AppState.SOLVING)
          }
        }
      }
      AppState.SOLVING -> p { +"Solving, please wait..." }
      AppState.SHOWING_RESULT -> {
        if (gridLetters.isNotEmpty()) {
          p { +"Found a solution:" }
          Grid {
            this.letters = gridLetters
          }
        } else {
          p { +"Sorry, could not find a solution." }
        }
        p {
          Button {
            sx {
              marginTop = 16.px
            }
            +"Reset"
            variant = ButtonVariant.outlined
            onClick = {
              inputLetters = ""
              gridLetters = emptyList()
              props.onAppStateUpdate(AppState.WAITING_FOR_INPUT)
            }
          }
        }
      }
    }
  }
}
