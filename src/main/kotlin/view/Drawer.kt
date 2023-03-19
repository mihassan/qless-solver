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
import model.AppState.Companion.loadDictionary
import model.AppState.Companion.solve
import model.Configuration
import model.ModalState
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

private const val MAX_SOLVE_HISTORY = 10

val Drawer = FC<Props> {
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  var modalState by useContext(ModalStateContext)
  var configuration by useContext(ConfigurationContext)
  var dictionary by useContext(DictionaryContext)
  var solveHistory by useContext(SolveHistoryContext)
  var bannedWords by useContext(BannedWordsContext)

  useEffectOnce {
    var (dictionaryType, dictionarySize, strategy) = configuration
    window.localStorage.getItem("dictionaryType")?.let {
      dictionaryType = DictionaryType.valueOf(it)
    }
    window.localStorage.getItem("dictionarySize")?.let {
      dictionarySize = DictionarySize.valueOf(it)
    }
    window.localStorage.getItem("strategy")?.let {
      strategy = Strategy.valueOf(it)
    }
    configuration = Configuration(dictionaryType, dictionarySize, strategy)
  }

  useEffect(configuration.dictionaryType, configuration.dictionarySize) {
    appState = appState.loadDictionary()
    mainScope.launch {
      dictionary =
        DictionaryLoader(configuration.dictionaryType, configuration.dictionarySize).load()

      window.localStorage.setItem("dictionaryType", configuration.dictionaryType.name)
      window.localStorage.setItem("dictionarySize", configuration.dictionarySize.name)

      modalState = modalState.closeDrawer()
      appState = appState.solve()
    }
  }

  useEffect(configuration.strategy) {
    window.localStorage.setItem("strategy", configuration.strategy.name)

    modalState = modalState.closeDrawer()
    appState = appState.solve()
  }

  useEffectOnce {
    window.localStorage.getItem("solveHistory")?.let {
      solveHistory = it
        .split(", ")
        .filter(String::isNotBlank)
        .take(MAX_SOLVE_HISTORY)
        .toSet()
    }
  }

  useEffect(solveHistory) {
    window.localStorage.setItem("solveHistory", solveHistory.joinToString())
  }

  useEffectOnce {
    window.localStorage.getItem("bannedWords")?.let {
      bannedWords = it
        .split(", ")
        .filter(String::isNotBlank)
        .toSet()
    }
  }

  useEffect(bannedWords) {
    window.localStorage.setItem("bannedWords", bannedWords.joinToString())
  }

  SwipeableDrawer {
    anchor = left
    open = modalState == ModalState.DRAWER
    onClose = { modalState = modalState.closeDrawer() }

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
              value = configuration.dictionaryType
              onChange = { event, _ ->
                configuration =
                  configuration.copy(dictionaryType = DictionaryType.valueOf(event.target.value))
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
              value = configuration.dictionarySize
              onChange = { event, _ ->
                configuration =
                  configuration.copy(dictionarySize = DictionarySize.valueOf(event.target.value))
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
              value = configuration.strategy
              onChange = { event, _ ->
                configuration = configuration.copy(strategy = Strategy.valueOf(event.target.value))
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
      if (solveHistory.isNotEmpty()) {
        Divider {}
        List {
          subheader = ListSubheader.create {
            +"History"
          }
          solveHistory.reversed().forEach { inputLetters ->
            ListItemButton {
              onClick = {
                modalState = modalState.closeDrawer()
                appState = appState.solve(inputLetters)
              }
              ListItemText {
                +inputLetters
              }
            }
          }
          ListItemButton {
            onClick = { solveHistory = emptySet() }
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
      if (bannedWords.isNotEmpty()) {
        Divider {}
        List {
          subheader = ListSubheader.create {
            +"Banned words"
          }
          bannedWords.forEach { word ->
            ListItemButton {
              onClick = {
                bannedWords = bannedWords - word
                modalState = modalState.closeDrawer()
                appState = appState.solve()
              }
              ListItemText {
                +word
              }
            }
          }
          ListItemButton {
            onClick = {
              bannedWords = emptySet()
              modalState = modalState.closeDrawer()
              appState = appState.solve()
            }
            ListItemText {
              Typography {
                sx {
                  color = Color("error.main")
                }
                +"Clear Banned Words"
              }
            }
          }
        }
      }
    }
  }
}
