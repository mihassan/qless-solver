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
  var onReset: () -> Unit
  var onSubmit: (String) -> Unit
}

val InputForm = FC<InputFormProps> { props ->
  var inputLetters by useState("")

  TextField {
    autoFocus = true
    variant = FormControlVariant.standard
    value = inputLetters
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
