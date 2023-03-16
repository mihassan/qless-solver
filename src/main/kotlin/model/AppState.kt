package model

enum class AppState(val displayText: String) {
  PAGE_OPENED("page opened"),
  LOADING_DICTIONARY("loading dictionary"),
  WAITING_FOR_INPUT("waiting for input"),
  SOLVING("solving..."),
  SHOWING_RESULT("showing result"),
}
