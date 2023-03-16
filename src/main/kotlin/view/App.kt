package view

import csstype.AlignItems
import csstype.Auto
import csstype.Display
import csstype.array
import csstype.dvh
import csstype.fr
import model.Dictionary
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.useState

val App = FC<Props> {
  var dictionary by useState { Dictionary.of("") }
  var inputLetters by useState { "" }
  var showDrawer by useState { false }
  var showHelpDialog by useState { false }

  BaseModule {
    Box {
      sx {
        display = Display.grid
        gridTemplateRows = array(Auto.auto, 1.fr, Auto.auto)
        alignItems = AlignItems.center
        height = 100.dvh
      }

      gap = 2

      Header {
        this.toggleDrawer = {
          showDrawer = !showDrawer
        }
        this.toggleHelpDialog = {
          showHelpDialog = !showHelpDialog
        }
      }

      Drawer {
        this.isOpen = showDrawer
        this.onClose = { showDrawer = false }
        this.onInputUpdate = { inputLetters = it }
        this.onDictionaryUpdate = { dictionary = it }
      }

      Content {
        this.dictionary = dictionary
        this.inputLetters = inputLetters
        this.onInputUpdate = { inputLetters = it }
      }

      Footer {}

      HelpDialog {
        isOpen = showHelpDialog
        onClose = { showHelpDialog = false }
      }
    }
  }
}
