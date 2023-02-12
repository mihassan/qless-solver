package view

import csstype.NamedColor
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2

val Banner = FC<Props> {
  h2 {
    css {
      padding = 10.px
      backgroundColor = NamedColor.lightblue
    }
    +"Q-Less Solver"
  }
}