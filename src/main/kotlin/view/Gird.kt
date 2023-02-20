package view

import csstype.AlignItems
import csstype.Display
import csstype.FontSize
import csstype.FontWeight
import csstype.JustifyContent
import csstype.NamedColor
import csstype.fr
import csstype.px
import csstype.vmin
import model.Point
import mui.material.Grid
import mui.material.Paper
import mui.system.sx
import react.FC
import react.Props
import react.useState

external interface GridProps : Props {
  var letters: List<List<String>>
}

val Grid = FC<GridProps> { props ->
  val rowCount = props.letters.size
  val colCount = props.letters.firstOrNull()?.size ?: 0
  var highlightedCells by useState<Set<Point>>(emptySet())

  Grid {
    sx {
      display = Display.inlineGrid
      gap = 8.px
      gridTemplateRows = csstype.repeat(rowCount, 1.fr)
      gridTemplateColumns = csstype.repeat(colCount, 1.fr)
    }
    props.letters.forEachIndexed { y, row ->
      row.forEachIndexed { x, letter ->
        val isHighlighted = Point(x, y) in highlightedCells
        Paper {
          sx {
            display = Display.flex
            width = 8.vmin
            height = 8.vmin
            fontSize = FontSize.xLarge
            fontWeight = FontWeight.bold
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            backgroundColor =
              when {
                letter.isBlank() -> NamedColor.lightgray
                isHighlighted -> NamedColor.lightcyan
                else -> NamedColor.white
              }
          }
          elevation =
            when {
              letter.isBlank() -> 0
              isHighlighted -> 12
              else -> 3
            }
          +letter
          onMouseEnter = {
            highlightedCells = buildSet {
              (y downTo 0)
                .takeWhile { props.letters[it][x].isNotBlank() }
                .forEach { add(Point(x, it)) }
              (y until rowCount)
                .takeWhile { props.letters[it][x].isNotBlank() }
                .forEach { add(Point(x, it)) }
              (x downTo 0)
                .takeWhile { props.letters[y][it].isNotBlank() }
                .forEach { add(Point(it, y)) }
              (x until colCount)
                .takeWhile { props.letters[y][it].isNotBlank() }
                .forEach { add(Point(it, y)) }
            }
          }
        }
      }
    }
    onMouseLeave = {
      highlightedCells = emptySet()
    }
  }
}
