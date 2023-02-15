package view

import csstype.px
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.FormControl
import mui.material.Input
import mui.material.InputLabel
import mui.system.sx
import react.FC
import react.Props
import react.useState
import web.html.HTMLInputElement

external interface InputFormProps : Props {
  var onSubmit: (String) -> Unit
}

val InputForm = FC<InputFormProps> { props ->
  var inputLetters by useState("")

  FormControl {
    InputLabel {
      htmlFor = "inputLetters"
      +"Type input letters: "
    }
    Input {
      id = "inputLetters"
      onKeyDown = { event ->
        if (event.key.any { !it.isLetter() }) {
          event.preventDefault()
        }
      }
      onChange = { event ->
        inputLetters = (event.target as HTMLInputElement).value
      }
      onSubmit = {
        props.onSubmit(inputLetters)
      }
    }
    Button {
      sx {
        marginTop = 16.px
      }
      variant = ButtonVariant.outlined
      +"Solve"
      onClick = { event ->
        props.onSubmit(inputLetters)
        event.preventDefault()
      }
    }
  }
}
