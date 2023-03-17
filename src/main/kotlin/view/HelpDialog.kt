package view

import csstype.ListStylePosition
import emotion.react.css
import model.ModalState
import mui.material.Dialog
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.DialogTitle
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.useContext

val HelpDialog = FC<Props> {
  var modalState by useContext(ModalStateContext)

  Dialog {
    open = modalState == ModalState.HELP_DIALOG
    onClose = { _, _ ->
      modalState = modalState.closeHelpDialog()
    }
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
