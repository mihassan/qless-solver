package view

import csstype.NamedColor
import csstype.TextAlign
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2

val Header = FC<Props> {
  h2 {
    css {
      padding = 10.px
      backgroundColor = NamedColor.lightblue
      textAlign = TextAlign.center
    }
    +"Q-Less Solver"
  }
}
