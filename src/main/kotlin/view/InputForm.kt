package view

import csstype.FontSize
import csstype.FontWeight
import csstype.TextAlign
import csstype.px
import js.core.jso
import model.AppState
import model.AppState.Companion.getInputLetters
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.events.FormEvent
import react.dom.onChange
import react.useContext
import web.html.HTMLDivElement
import web.html.HTMLInputElement

val InputForm = FC<Props> {
  var appState by useContext(AppStateContext)

  TextField {
    autoFocus = true
    disabled = appState is AppState.Solving
    variant = FormControlVariant.standard
    value = appState.getInputLetters()
    InputProps = jso {
      inputProps = jso {
        style = jso {
          fontSize = FontSize.xLarge
          fontWeight = FontWeight.bold
          textAlign = TextAlign.center
          letterSpacing = 4.px
        }
      }
    }
    onKeyDown = { event ->
      if (event.key == "Enter") {
        appState = AppState.Solving(appState.getInputLetters())
      }
    }
    onChange = { event ->
      val newInputLetters = event.validateInput()
      if (appState.getInputLetters() != newInputLetters) {
        appState = AppState.WaitingForInput(newInputLetters)
      }
    }
  }
}

private fun FormEvent<HTMLDivElement>.validateInput() =
  (this.target as HTMLInputElement)
    .value
    .filter { it in 'a'..'z' || it in 'A'..'Z' }
    .uppercase()
    .take(12)
