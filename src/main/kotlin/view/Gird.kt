package view

import controller.WordDefinitions
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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.AppState.Companion.solve
import model.Board
import model.Point
import mui.icons.material.Block
import mui.icons.material.ContentCopy
import mui.icons.material.TableView
import mui.material.Card
import mui.material.CardContent
import mui.material.Divider
import mui.material.Grid
import mui.material.ListItemIcon
import mui.material.ListItemText
import mui.material.Menu
import mui.material.MenuItem
import mui.material.Paper
import mui.material.PopoverReference
import mui.material.Popper
import mui.material.Stack
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useContext
import react.useEffectOnce
import react.useState
import web.dom.Element
import web.navigator.navigator

external interface GridProps : Props {
  var board: Board
}

val Grid = FC<GridProps> { props ->
  val mainScope = MainScope()
  var appState by useContext(AppStateContext)
  var definitions by useState<Map<String, String>>(emptyMap())
  var bannedWords by useContext(BannedWordsContext)
  var highlightedCells by useState<Set<Point>>(emptySet())
  var highlightedWords by useState<Set<String>>(emptySet())
  var contextMenuPos by useState<Pair<Double, Double>>()
  var definitionDialogEl by useState<Element>()

  useEffectOnce {
    mainScope.launch {
      definitions = props.board.words(3).mapNotNull { placedWord ->
        WordDefinitions.fetch(placedWord.word)?.let { definition ->
          placedWord.word to definition
        }
      }.toMap()
    }
  }


  Grid {
    sx {
      display = Display.inlineGrid
      gap = 8.px
      gridTemplateRows = csstype.repeat(props.board.rowCount(), 1.fr)
      gridTemplateColumns = csstype.repeat(props.board.columnCount(), 1.fr)
    }
    tabIndex = 0

    onMouseLeave = {
      highlightedCells = emptySet()
      definitionDialogEl = null
    }
    onContextMenu = { event ->
      event.preventDefault()
      if (contextMenuPos == null) contextMenuPos = event.clientX to event.clientY
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
          onMouseEnter = { event ->
            highlightedCells = props.board.getConnectedCells(currentCell, 3)
            highlightedWords = props.board.getConnectedWords(currentCell, 3)
            definitionDialogEl = event.currentTarget.takeIf { letter != null }
          }
          +"${letter ?: ' '}"
        }
      }
    }

    Popper {
      open = definitionDialogEl != null && highlightedWords.isNotEmpty()
      anchorEl = definitionDialogEl
      Stack {
        sx {
          maxWidth = 360.px
          gap = 8.px
        }
        highlightedWords.forEach { word ->
          Card {
            CardContent {
              Typography {
                gutterBottom = true
                variant = TypographyVariant.h5
                component = ReactHTML.div
                +word
              }
              Typography {
                sx {
                  color =
                    if (word in definitions) Color("text.secondary") else Color("error.main")
                }
                variant = TypographyVariant.body2
                +(definitions[word] ?: "Could not load definition.")
              }
            }
          }
        }
      }
    }

    Menu {
      open = contextMenuPos != null
      onClose = { contextMenuPos = null }
      anchorReference = PopoverReference.anchorPosition
      anchorPosition = contextMenuPos?.let {
        jso {
          top = it.second
          left = it.first
        }
      }
      MenuItem {
        onClick = {
          navigator.clipboard.writeText(props.board.show())
          contextMenuPos = null
        }
        ListItemIcon {
          ContentCopy {}
        }
        ListItemText {
          +"Copy"
        }
      }
      MenuItem {
        onClick = {
          navigator.clipboard.writeText(props.board.showAsMarkDown())
          contextMenuPos = null
        }
        ListItemIcon {
          TableView {}
        }
        ListItemText {
          +"Copy as MarkDown"
        }
      }
      if (highlightedWords.isNotEmpty()) {
        Divider {}
        highlightedWords.forEach { word ->
          MenuItem {
            onClick = {
              bannedWords = bannedWords + setOf(word)
              contextMenuPos = null
              appState = appState.solve()
            }
            ListItemIcon {
              Block {}
            }
            ListItemText {
              +"Ban word: $word"
            }
          }
        }
      }
    }
  }
}
