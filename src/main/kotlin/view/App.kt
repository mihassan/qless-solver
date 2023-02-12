package view

import controller.DictionaryLoader
import controller.Solver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Dictionary
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.p
import react.useEffect
import react.useEffectOnce
import react.useState

enum class AppState {
  PAGE_OPENED,
  LOADING_DICTIONARY,
  WAITING_FOR_INPUT,
  SOLVING,
  SHOWING_RESULT,
}

val App = FC<Props> {
  val mainScope = MainScope()
  var state by useState { AppState.PAGE_OPENED }
  var dictionary by useState { Dictionary.of("") }
  var inputLetters by useState { "" }
  var gridLetters: List<List<String>> by useState { emptyList() }

  useEffectOnce {
    state = AppState.LOADING_DICTIONARY
    mainScope.launch {
      dictionary = DictionaryLoader.loadDictionary()
      state = AppState.WAITING_FOR_INPUT
    }
  }

  useEffect {
    if (state == AppState.SOLVING) {
      mainScope.launch {
        delay(100)
        if (gridLetters.isEmpty()) {
          val result = Solver(dictionary).solve(inputLetters)
          if (result != null) {
            gridLetters = result.lines().map { it.map { "$it" } }
          }
        }
        state = AppState.SHOWING_RESULT
      }
    }
  }

  +Banner.create()

  when (state) {
    AppState.PAGE_OPENED -> p { +"Welcome" }
    AppState.LOADING_DICTIONARY -> p { +"Loading dictionary..." }
    AppState.WAITING_FOR_INPUT -> {
      +InputForm.create {
        onSubmit = {
          inputLetters = it
          state = AppState.SOLVING
        }
      }
    }
    AppState.SOLVING -> p { +"Solving, please wait..." }
    AppState.SHOWING_RESULT -> {
      if (gridLetters.isNotEmpty()) {
        p { +"Found a solution:" }
        +Grid.create {
          this.letters = gridLetters
        }
      } else {
        p { +"Sorry, could not find a solution." }
      }
      p {
        button {
          +"Reset"
          onClick = {
            inputLetters = ""
            gridLetters = emptyList()
            state = AppState.WAITING_FOR_INPUT
          }
        }
      }
    }
  }

  +Footer.create {
    appState = state
  }
}
