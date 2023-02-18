package view

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
  var onReset: () -> Unit
  var onSubmit: (String) -> Unit
}

val InputForm = FC<InputFormProps> { props ->
  var inputLetters by useState("")

  TextField {
    autoFocus = true
    variant = FormControlVariant.standard
    value = inputLetters
    onKeyDown = { event ->
      if (event.key == "Enter") {
        props.onSubmit(inputLetters)
      }
    }
    onChange = { event ->
      val newInputLetters = event.validateInput()
      if (inputLetters != newInputLetters) {
        inputLetters = newInputLetters
        props.onReset()
      }
    }
  }
}

private fun FormEvent<HTMLDivElement>.validateInput() =
  (this.target as HTMLInputElement)
    .value
    .filter(Char::isLetter)
    .uppercase()
    .take(12)
