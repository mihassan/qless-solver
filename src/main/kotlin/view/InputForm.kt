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
import web.events.EventTarget

external interface InputFormProps : Props {
  var onSubmit: (String) -> Unit
}

private val EventTarget.value: String
  get() {
    return asDynamic().value
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
      onChange = { event ->
        inputLetters = event.target.value
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
