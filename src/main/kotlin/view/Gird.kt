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
import js.core.jso
import model.AppState.Companion.solve
import model.Board
import model.Point
import mui.material.Divider
import mui.material.Grid
import mui.material.Menu
import mui.material.MenuItem
import mui.material.Paper
import mui.material.PopoverReference
import mui.system.sx
import react.FC
import react.Props
import react.useContext
import react.useState
import web.navigator.navigator

external interface GridProps : Props {
  var board: Board
}

val Grid = FC<GridProps> { props ->
  var appState by useContext(AppStateContext)
  var bannedWords by useContext(BannedWordsContext)
  var highlightedCells by useState<Set<Point>>(emptySet())
  var highlightedWords by useState<Set<String>>(emptySet())
  var contextMenu by useState<Pair<Double, Double>>()

  Grid {
    sx {
      display = Display.inlineGrid
      gap = 8.px
      gridTemplateRows = csstype.repeat(props.board.rowCount(), 1.fr)
      gridTemplateColumns = csstype.repeat(props.board.columnCount(), 1.fr)
    }
    tabIndex = 0
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
            highlightedWords = props.board.getConnectedWords(currentCell, 3)
          }
          +"${letter ?: ' '}"
        }
      }
    }
    Menu {
      open = contextMenu != null
      onClose = { contextMenu = null }
      anchorReference = PopoverReference.anchorPosition
      anchorPosition = contextMenu?.let {
        jso {
          top = contextMenu!!.second
          left = contextMenu!!.first
        }
      }
      MenuItem {
        onClick = {
          navigator.clipboard.writeText(props.board.show())
          contextMenu = null
        }
        +"Copy"
      }
      MenuItem {
        onClick = {
          navigator.clipboard.writeText(props.board.showAsMarkDown())
          contextMenu = null
        }
        +"Copy as MarkDown"
      }
      if (highlightedWords.isNotEmpty()) {
        Divider {}
        highlightedWords.forEach { word ->
          MenuItem {
            onClick = {
              bannedWords = bannedWords + setOf(word)
              contextMenu = null
              appState = appState.solve()
            }
            +"Ban word: $word"
          }
        }
      }
    }
    onMouseLeave = {
      highlightedCells = emptySet()
    }
    onContextMenu = { event ->
      event.preventDefault()
      if (contextMenu == null) contextMenu = event.clientX to event.clientY
    }
  }
}
