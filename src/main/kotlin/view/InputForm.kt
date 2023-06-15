package view

import js.core.jso
import model.AppState
import model.AppState.Companion.editInput
import model.AppState.Companion.getInputLetters
import model.AppState.Companion.solve
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.onChange
import react.useContext
import web.cssom.FontSize
import web.cssom.FontWeight
import web.cssom.TextAlign
import web.cssom.px
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
        appState = appState.solve()
      }
    }
    onChange = { event ->
      appState = appState.editInput((event.target as HTMLInputElement).value)
    }
  }
}

