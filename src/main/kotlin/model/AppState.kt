package model

sealed class AppState(val displayText: String) {
  object PageOpened : AppState("page opened")
  object LoadingDictionary : AppState("loading dictionary")
  data class WaitingForInput(val inputLetters: String) : AppState("waiting for input")
  data class Solving(val inputLetters: String) : AppState("solving...")
  data class ShowingResult(val inputLetters: String, val board: Board) : AppState("showing result")
  data class NoSolutionFound(val inputLetters: String) : AppState("no solution found")

  companion object {
    fun AppState.getInputLetters(): String = when (this) {
      PageOpened -> ""
      LoadingDictionary -> ""
      is WaitingForInput -> inputLetters
      is Solving -> inputLetters
      is ShowingResult -> inputLetters
      is NoSolutionFound -> inputLetters
    }

    fun AppState.loadDictionary(): AppState = LoadingDictionary

    fun AppState.editInput(inputLetters: String): AppState {
      val validInputLetters = inputLetters
        .filter { it in 'a'..'z' || it in 'A'..'Z' }
        .uppercase()
        .take(12)

      return if (getInputLetters() != validInputLetters)
        WaitingForInput(validInputLetters)
      else
        this
    }

    fun AppState.solve(inputLetters: String): AppState = when (this) {
      PageOpened -> this
      LoadingDictionary -> this
      is Solving -> this
      else -> Solving(inputLetters)
    }

    fun AppState.solve(): AppState = when (this) {
      PageOpened -> this
      LoadingDictionary -> this
      is Solving -> this
      is NoSolutionFound -> Solving(inputLetters)
      is ShowingResult -> Solving(inputLetters)
      is WaitingForInput -> Solving(inputLetters)
    }

    fun AppState.showResult(board: Board?): AppState = when (this) {
      PageOpened -> this
      LoadingDictionary -> this
      is WaitingForInput -> this
      is Solving ->
        if (board == null) NoSolutionFound(inputLetters) else ShowingResult(inputLetters, board)
      is ShowingResult ->
        if (board == null) NoSolutionFound(inputLetters) else ShowingResult(inputLetters, board)
      is NoSolutionFound ->
        if (board == null) NoSolutionFound(inputLetters) else ShowingResult(inputLetters, board)
    }
  }
}
