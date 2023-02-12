package view

import csstype.FontFamily
import csstype.FontSize
import csstype.FontWeight
import csstype.NamedColor
import csstype.px
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
                fontSize = FontSize.large
                fontWeight = FontWeight.bolder
                padding = 10.px
                color = NamedColor.black
                backgroundColor =
                  if (letter.isNotBlank()) NamedColor.lightgray else NamedColor.darkgray
              }
              +letter
            }
          }
        }
      }
    }
  }
}
