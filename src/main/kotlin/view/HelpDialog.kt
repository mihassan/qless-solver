package view

import csstype.ListStylePosition
import emotion.react.css
import mui.material.Dialog
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul

external interface HelpDialogProps : Props {
  var isOpen: Boolean
  var onClose: () -> Unit
}

val HelpDialog = FC<HelpDialogProps> { props ->
  Dialog {
    open = props.isOpen
    onClose = { _, _ -> props.onClose() }
    DialogTitle {
      +"How to Use"
    }
    DialogContent {
      DialogContentText {
        +"This is a simple web app that solves the Q-less word game, a crossword solitaire game."
      }
      p {
        ul {
          css {
            listStylePosition = ListStylePosition.inside
          }
          li {
            +"Enter 12 letters in the input box and press enter."
          }
          li {
            +"If a solution is found, it will be shown in a grid."
          }
          li {
            +"Edit the input field to reset the solution and try another combination."
          }
        }
      }
    }
  }
}
