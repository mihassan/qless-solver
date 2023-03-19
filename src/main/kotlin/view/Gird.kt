package view

import csstype.AlignItems
import csstype.Color
import csstype.Display
import csstype.FontSize
import csstype.FontWeight
import csstype.JustifyContent
import csstype.UserSelect
import csstype.fr
import csstype.px
import csstype.vmin
import model.Board
import model.Point
import mui.material.Grid
import mui.material.Paper
import mui.system.sx
import react.FC
import react.Props
import react.useState

external interface GridProps : Props {
  var board: Board
}

val Grid = FC<GridProps> { props ->
  var highlightedCells by useState<Set<Point>>(emptySet())

  Grid {
    sx {
      display = Display.inlineGrid
      gap = 8.px
      gridTemplateRows = csstype.repeat(props.board.rowCount(), 1.fr)
      gridTemplateColumns = csstype.repeat(props.board.columnCount(), 1.fr)
    }
    props.board.yRange().forEach { y ->
      props.board.xRange().forEach { x ->
        val currentCell = Point(x, y)
        val letter = props.board.cells[currentCell]
        val isHighlighted = currentCell in highlightedCells

        Paper {
          sx {
            display = Display.flex
            width = 8.vmin
            height = 8.vmin
            fontSize =
              when {
                isHighlighted -> FontSize.xLarge
                else -> FontSize.large
              }
            fontWeight =
              when {
                isHighlighted -> FontWeight.bolder
                else -> FontWeight.normal
              }
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            backgroundColor =
              when (letter) {
                null -> Color("action.disabledBackground")
                else -> Color("background.default")
              }
            userSelect = "none".unsafeCast<UserSelect>()
          }
          elevation =
            when {
              letter == null -> 1
              isHighlighted -> 12
              else -> 6
            }
          square = true
          onMouseEnter = {
            highlightedCells = props.board.getConnectedCells(currentCell, 3)
          }
          +"${letter ?: ' '}"
        }
      }
    }
    onMouseLeave = {
      highlightedCells = emptySet()
    }
  }
}
