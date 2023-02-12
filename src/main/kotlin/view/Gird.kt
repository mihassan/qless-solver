package view

import csstype.FontFamily
import csstype.px
import csstype.rgb
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr

external interface GridProps : Props {
  var letters: List<List<String>>
}

val Grid = FC<GridProps> { props ->
  table {
    css {
      paddingTop = 20.px
      paddingBottom = 20.px
    }
    tbody {
      props.letters.forEach { row ->
        tr {
          row.forEach { letter ->
            td {
              css {
                fontFamily = FontFamily.monospace
                padding = 5.px
                color = rgb(31, 63, 0)
                backgroundColor = rgb(167, 167, 167)
              }
              +letter
            }
          }
        }
      }
    }
  }
}
