package view

import csstype.AlignItems
import csstype.Display
import csstype.FontSize
import csstype.FontWeight
import csstype.JustifyContent
import csstype.NamedColor
import csstype.PlaceItems
import csstype.fr
import csstype.px
import csstype.vmin
import mui.material.Grid
import mui.material.Paper
import mui.system.sx
import react.FC
import react.Props

external interface GridProps : Props {
  var letters: List<List<String>>
}

val Grid = FC<GridProps> { props ->
  val rowCount = props.letters.size
  val colCount = props.letters.firstOrNull()?.size ?: 0

  Grid {
    sx {
      display = Display.inlineGrid
      margin = 16.px
      gap = 8.px
      gridTemplateRows = csstype.repeat(rowCount, 1.fr)
      gridTemplateColumns = csstype.repeat(colCount, 1.fr)
      placeItems = PlaceItems.center
    }
    props.letters.forEach { row ->
      row.forEach { letter ->
        Paper {
          sx {
            display = Display.flex
            width = 8.vmin
            height = 8.vmin
            fontSize = FontSize.xLarge
            fontWeight = FontWeight.bold
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            backgroundColor = if (letter.isNotBlank()) NamedColor.lightgray else NamedColor.darkgray
          }
          elevation = 1
          +letter
        }
      }
    }
  }
}
