package view

import csstype.AlignItems
import csstype.Auto
import csstype.Display
import csstype.array
import csstype.dvh
import csstype.fr
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props

val App = FC<Props> {
  BaseModule {
    Box {
      sx {
        display = Display.grid
        gridTemplateRows = array(Auto.auto, 1.fr, Auto.auto)
        alignItems = AlignItems.center
        height = 100.dvh
      }

      gap = 2

      Header {}

      Drawer {}

      Content {}

      Footer {}

      HelpDialog {}
    }
  }
}
