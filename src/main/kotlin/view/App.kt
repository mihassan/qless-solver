package view

import controller.Strategy
import csstype.AlignItems
import csstype.Auto
import csstype.Display
import csstype.array
import csstype.dvh
import csstype.fr
import kotlinx.browser.window
import model.Dictionary
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.useEffect
import react.useEffectOnce
import react.useState

val App = FC<Props> {
  var dictionary by useState { Dictionary.of("") }
  var inputLetters by useState { "" }
  var solveHistory by useState { emptySet<String>() }
  var showDrawer by useState { false }
  var showHelpDialog by useState { false }

  useEffectOnce {
    window.localStorage.getItem("solveHistory")?.let {
      solveHistory = it.split(", ").filter { it.isNotBlank() }.take(10).toSet()
    }
  }

  useEffect(solveHistory) {
    window.localStorage.setItem("solveHistory", solveHistory.joinToString())
  }

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
        this.solveHistory = solveHistory
        this.clearSolveHistory = { solveHistory = emptySet() }
        this.onInputUpdate = { inputLetters = it }
        this.onDictionaryUpdate = { dictionary = it }
      }

      Content {
        this.onSolve = { solveHistory = solveHistory - inputLetters + inputLetters }
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
