package view

import controller.DictionarySize
import controller.DictionaryType
import controller.Strategy
import csstype.rem
import mui.material.DrawerAnchor.left
import mui.material.FormControl
import mui.material.FormControlVariant
import mui.material.InputLabel
import mui.material.List
import mui.material.ListItem
import mui.material.MenuItem
import mui.material.Select
import mui.material.SwipeableDrawer
import mui.material.Toolbar
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props

external interface DrawerProps : Props {
  var isOpen: Boolean
  var onClose: () -> Unit
  var dictionaryType: DictionaryType
  var onDictionaryTypeUpdate: (DictionaryType) -> Unit
  var dictionarySize: DictionarySize
  var onDictionarySizeUpdate: (DictionarySize) -> Unit
  var strategy: Strategy
  var onStrategyUpdate: (Strategy) -> Unit
}

val Drawer = FC<DrawerProps> { props ->
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
              value = props.dictionaryType
              onChange = { event, _ ->
                val dictionaryType = DictionaryType.valueOf(event.target.value)
                props.onDictionaryTypeUpdate(dictionaryType)
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
              value = props.dictionarySize
              onChange = { event, _ ->
                val dictionarySize = DictionarySize.valueOf(event.target.value)
                props.onDictionarySizeUpdate(dictionarySize)
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
                val strategy = Strategy.valueOf(event.target.value)
                props.onStrategyUpdate(strategy)
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
    }
  }
}
