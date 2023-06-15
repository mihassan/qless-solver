package view

import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import web.cssom.AlignItems
import web.cssom.Auto
import web.cssom.Display
import web.cssom.array
import web.cssom.dvh
import web.cssom.fr

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
