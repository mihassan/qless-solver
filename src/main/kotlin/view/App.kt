package view

import react.FC
import react.Props
import react.useState

enum class AppState {
  PAGE_OPENED,
  LOADING_DICTIONARY,
  WAITING_FOR_INPUT,
  SOLVING,
  SHOWING_RESULT,
}

val App = FC<Props> {
  var state by useState { AppState.PAGE_OPENED }

  Header()
  Content {
    appState = state
    onAppStateUpdate = {
      state = it
    }
  }

  Footer {
    appState = state
  }
}
