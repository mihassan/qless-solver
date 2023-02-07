package view

import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1

val Banner = FC<Props> {
  h1 {
    css {
      padding = 5.px
    }
    +"Q-Less Solver"
  }
}