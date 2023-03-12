package view

import controller.DictionaryLoader
import controller.DictionarySize
import csstype.AlignItems
import csstype.Auto
import csstype.Display
import csstype.array
import csstype.dvh
import csstype.fr
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Dictionary
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.useEffect
import react.useEffectOnce
import react.useState

enum class AppState(val displayText: String) {
  PAGE_OPENED("page opened"),
  LOADING_DICTIONARY("loading dictionary"),
  WAITING_FOR_INPUT("waiting for input"),
  SOLVING("solving..."),
  SHOWING_RESULT("showing result"),
}

val App = FC<Props> {
  val mainScope = MainScope()
  var state by useState { AppState.PAGE_OPENED }
  var dictionarySize by useState { DictionarySize.Small }
  var dictionary by useState { Dictionary.of("") }
  var showDrawer by useState { false }
  var showHelpDialog by useState { false }

  useEffectOnce {
    window.localStorage.getItem("dictionarySize")?.let {
      dictionarySize = DictionarySize.valueOf(it)
    }
  }

  useEffect(dictionarySize) {
    state = AppState.LOADING_DICTIONARY
    mainScope.launch {
      dictionary = DictionaryLoader.loadDictionary(dictionarySize)
      window.localStorage.setItem("dictionarySize", dictionarySize.name)
      state = AppState.WAITING_FOR_INPUT
    }
  }

  ThemeModule {
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
        this.dictionarySize = dictionarySize
        this.onDictionarySizeUpdate = { dictionarySize = it }
      }

      Content {
        this.appState = state
        this.onAppStateUpdate = { state = it }
        this.dictionary = dictionary
      }

      Footer {
        this.appState = state
      }

      HelpDialog {
        isOpen = showHelpDialog
        onClose = { showHelpDialog = false }
      }
    }
  }
}
