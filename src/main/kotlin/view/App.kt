package view

import controller.DictionaryLoader
import controller.Solver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Dictionary
import react.FC
import react.Props
import react.create
import react.useEffect
import react.useEffectOnce
import react.useState

enum class AppState {
  STARTED,
  LOADING,
  WAITING,
  SOLVING,
  SHOWING,
}

val App = FC<Props> {
  val mainScope = MainScope()
  var state by useState { AppState.STARTED }
  var dictionary by useState { Dictionary.of("") }
  var inputLetters by useState { "" }
  var letters: List<List<String>> by useState { emptyList() }

  useEffectOnce {
    state = AppState.LOADING
    mainScope.launch {
      dictionary = DictionaryLoader.loadDictionary()
      state = AppState.WAITING
    }
  }

  useEffect(state) {
    if (state == AppState.SOLVING) {
      mainScope.launch {
        val result = Solver(dictionary).solve(inputLetters)
        if (result != null) {
          letters = result.lines().map { it.map { "$it" } }
        }
        state = AppState.SHOWING
      }
    }
  }

  +Banner.create()
  when (state) {
    AppState.STARTED -> +"Welcome"
    AppState.LOADING -> +"Loading dictionary..."
    AppState.WAITING -> {
      +InputForm.create {
        onSubmit = {
          inputLetters = it
          state = AppState.SOLVING
        }
      }
    }
    AppState.SOLVING -> +"Solving, please wait..."
    AppState.SHOWING -> {
      if (letters.isNotEmpty()) {
        +Grid.create {
          this.letters = letters
        }
      } else {
        +"Sorry, could not find a solution."
      }
    }
  }
  +Footer.create {
    appState = state
  }
}
