package view

import csstype.FontSize
import csstype.FontWeight
import csstype.TextAlign
import csstype.px
import js.core.jso
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.events.FormEvent
import react.dom.onChange
import react.useState
import web.html.HTMLDivElement
import web.html.HTMLInputElement

external interface InputFormProps : Props {
  var appState: AppState
  var inputLetters: String
  var onInputUpdate: (String) -> Unit
  var onSubmit: () -> Unit
}

val InputForm = FC<InputFormProps> { props ->
  TextField {
    autoFocus = true
    disabled = props.appState == AppState.SOLVING
    variant = FormControlVariant.standard
    value = props.inputLetters
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
        props.onSubmit()
      }
    }
    onChange = { event ->
      val newInputLetters = event.validateInput()
      if (props.inputLetters != newInputLetters) {
        props.onInputUpdate(newInputLetters)
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
