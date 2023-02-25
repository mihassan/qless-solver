package view

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
import react.useState

external interface DrawerProps : Props {
  var isOpen: Boolean
  var onClose: () -> Unit
}

val Drawer = FC<DrawerProps> { props ->
  var dictionarySize by useState("small")
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
              +"Dictionary Size"
            }
            Select {
              value = dictionarySize
              onChange = { event, _ ->
                dictionarySize = event.target.value
              }
              MenuItem {
                value = "small"
                +"Small"
              }
              MenuItem {
                value = "medium"
                +"Medium"
              }
              MenuItem {
                value = "large"
                +"Large"
              }
            }
          }
        }
      }
    }
  }
}
