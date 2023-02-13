package view

import csstype.px
import emotion.react.css
import react.FC
import react.Props
import web.html.InputType
import react.dom.html.ButtonType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.form
import react.useState

external interface InputFormProps : Props {
  var onSubmit: (String) -> Unit
}

val InputForm = FC<InputFormProps> { props ->
  var inputLetters by useState("")

  form {
    label {
      htmlFor = "inputLetters"
      +"Type input letters: "
    }
    input {
      id = "inputLetters"
      type = InputType.text
      placeholder = "Type here"
      onChange = { event ->
        inputLetters = event.target.value
      }
    }
    button {
      css {
        marginLeft = 10.px
      }
      type = ButtonType.submit
      +"Solve"
    }
    onSubmit = { event ->
      props.onSubmit(inputLetters)
      event.preventDefault()
    }
  }
}