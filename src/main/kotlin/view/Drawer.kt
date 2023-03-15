package view

import controller.DictionaryLoader
import controller.DictionarySize
import controller.DictionaryType
import controller.Strategy
import csstype.Color
import csstype.rem
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Dictionary
import mui.material.Divider
import mui.material.DrawerAnchor.left
import mui.material.FormControl
import mui.material.FormControlVariant
import mui.material.InputLabel
import mui.material.List
import mui.material.ListItem
import mui.material.ListItemButton
import mui.material.ListItemText
import mui.material.ListSubheader
import mui.material.MenuItem
import mui.material.Select
import mui.material.SwipeableDrawer
import mui.material.Toolbar
import mui.material.Typography
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props
import react.create
import react.useContext
import react.useEffect
import react.useEffectOnce
import react.useState

external interface DrawerProps : Props {
  var isOpen: Boolean
  var onClose: () -> Unit
  var strategy: Strategy
  var onStrategyUpdate: (Strategy) -> Unit
  var solveHistory: Set<String>
  var clearSolveHistory: () -> Unit
  var onInputUpdate: (String) -> Unit
  var onDictionaryUpdate: (Dictionary) -> Unit
}

val Drawer = FC<DrawerProps> { props ->
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  var dictionaryType by useState { DictionaryType.QLess }
  var dictionarySize by useState { DictionarySize.Small }

  useEffectOnce {
    window.localStorage.getItem("dictionaryType")?.let {
      dictionaryType = DictionaryType.valueOf(it)
    }
    window.localStorage.getItem("dictionarySize")?.let {
      dictionarySize = DictionarySize.valueOf(it)
    }
    window.localStorage.getItem("strategy")?.let {
      props.onStrategyUpdate(Strategy.valueOf(it))
    }
  }

  useEffect(dictionaryType, dictionarySize) {
    appState = AppState.LOADING_DICTIONARY
    mainScope.launch {
      val dictionary = DictionaryLoader(dictionaryType, dictionarySize).load()
      props.onDictionaryUpdate(dictionary)

      window.localStorage.setItem("dictionaryType", dictionaryType.name)
      window.localStorage.setItem("dictionarySize", dictionarySize.name)

      props.onClose()
      appState = when (appState) {
        AppState.SHOWING_RESULT -> AppState.SOLVING
        else -> AppState.WAITING_FOR_INPUT
      }
    }
  }

  useEffect(props.strategy) {
    window.localStorage.setItem("strategy", props.strategy.name)

    props.onClose()
    appState = when (appState) {
      AppState.SHOWING_RESULT -> AppState.SOLVING
      else -> AppState.WAITING_FOR_INPUT
    }
  }

  SwipeableDrawer {
    anchor = left
    open = props.isOpen
    onClose = { props.onClose() }

    Box {
      Toolbar()
      List {
        ListItem {
          FormControl {
            variant = FormControlVariant.standard
            sx {
              minWidth = 10.rem
            }
            InputLabel {
              +"Dictionary type"
            }
            Select {
              value = dictionaryType
              onChange = { event, _ ->
                dictionaryType = DictionaryType.valueOf(event.target.value)
              }
              DictionaryType.values().map { type ->
                MenuItem {
                  value = "$type"
                  +"$type"
                }
              }
            }
          }
        }
        ListItem {
          FormControl {
            variant = FormControlVariant.standard
            sx {
              minWidth = 10.rem
            }
            InputLabel {
              +"Dictionary size"
            }
            Select {
              value = dictionarySize
              onChange = { event, _ ->
                dictionarySize = DictionarySize.valueOf(event.target.value)
              }
              DictionarySize.values().map { size ->
                MenuItem {
                  value = "$size"
                  +"$size"
                }
              }
            }
          }
        }
        ListItem {
          FormControl {
            variant = FormControlVariant.standard
            sx {
              minWidth = 10.rem
            }
            InputLabel {
              +"Solving strategy"
            }
            Select {
              value = props.strategy
              onChange = { event, _ ->
                props.onStrategyUpdate(Strategy.valueOf(event.target.value))
              }
              Strategy.values().map { strategy ->
                MenuItem {
                  value = "$strategy"
                  +strategy.display
                }
              }
            }
          }
        }
      }
      if (props.solveHistory.isNotEmpty()) {
        Divider {}
        List {
          subheader = ListSubheader.create {
            +"History"
          }
          props.solveHistory.reversed().forEach { inputLetters ->
            ListItemButton {
              onClick = {
                props.onInputUpdate(inputLetters)
                props.onClose()
                appState = AppState.SOLVING
              }
              ListItemText {
                +inputLetters
              }
            }
          }
          ListItemButton {
            onClick = { props.clearSolveHistory() }
            ListItemText {
              Typography {
                sx {
                  color = Color("error.main")
                }
                +"Clear History"
              }
            }
          }
        }
      }
    }
  }
}
