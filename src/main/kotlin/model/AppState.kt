package model

sealed class AppState(val displayText: String) {
  object PageOpened: AppState("page opened")
  object LoadingDictionary: AppState("loading dictionary")
  data class WaitingForInput(val inputLetters: String): AppState("waiting for input")
  data class Solving(val inputLetters: String): AppState("solving...")
  data class ShowingResult(val inputLetters: String, val board: Board): AppState("showing result")
  data class NoSolutionFound(val inputLetters: String): AppState("no solution found")

  companion object {
    fun AppState.getInputLetters(): String = when(this) {
      PageOpened -> ""
      LoadingDictionary -> ""
      is NoSolutionFound -> inputLetters
      is ShowingResult -> inputLetters
      is Solving -> inputLetters
      is WaitingForInput -> inputLetters
    }
  }
}
