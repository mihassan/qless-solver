package model

import util.Bag
import util.frequency
import util.groupContiguousBy
import util.isDistinct
import util.transpose

enum class Direction {
  Horizontal,
  Vertical
}

data class Point(val x: Int, val y: Int) {
  fun moveBy(dir: Direction, steps: Int): Point = when (dir) {
    Direction.Horizontal -> Point(x + steps, y)
    Direction.Vertical -> Point(x, y + steps)
  }
}

data class PlacedWord(val word: String, val position: Point, val direction: Direction) {
  val cells: List<Point> = when (direction) {
    Direction.Horizontal -> word.indices.map { Point(position.x + it, position.y) }
    Direction.Vertical -> word.indices.map { Point(position.x, position.y + it) }
  }
}

@Suppress("NOTHING_TO_INLINE")
data class Board(val cells: MutableMap<Point, Char> = mutableMapOf()) {
  inline fun clone(): Board = Board(cells.toMutableMap())

  inline fun letters(): Bag<Char> = cells.values.toList().frequency()

  inline fun topLeft(): Point =
    Point(
      cells.keys.minOfOrNull(Point::x) ?: 0,
      cells.keys.minOfOrNull(Point::y) ?: 0
    )

  inline fun bottomRight(): Point =
    Point(
      cells.keys.maxOfOrNull(Point::x) ?: 0,
      cells.keys.maxOfOrNull(Point::y) ?: 0
    )

  inline fun rowCount(): Int = bottomRight().y - topLeft().y + 1

  inline fun columnCount(): Int = bottomRight().x - topLeft().x + 1

  inline fun xRange(): IntRange = topLeft().x..bottomRight().x

  inline fun yRange(): IntRange = topLeft().y..bottomRight().y

  inline fun show(): String = yRange().joinToString("\n") { y ->
    xRange().map { x ->
      cells[Point(x, y)] ?: ' '
    }.joinToString("")
  }

  inline fun showAsMarkDown(): String {
    val lines = lines().map { it.map { " $it " } }
    val firstLine = lines[0]
    val restOfTheLines = lines.drop(1)
    val markdownRows = buildList {
      add(firstLine)
      add(firstLine.map { "---" })
      addAll(restOfTheLines)
    }
    return markdownRows.joinToString("\n") {
      it.joinToString("|", "|", "|")
    }
  }

  inline fun lines(): List<String> = show().lines()

  inline fun columns(): List<String> = lines().transpose()

  inline fun grid(): List<List<String>> = lines().map { it.map { "$it" } }

  fun words(minWordLength: Int): List<PlacedWord> {
    val topLeftX = topLeft().x
    val topLeftY = topLeft().y

    fun String.wordsWithIndex(): List<Pair<String, Int>> =
      withIndex()
        .toList()
        .groupContiguousBy { (_, c) -> c == ' ' }
        .filter { it.size >= minWordLength && it.first().value != ' ' }
        .map {
          val word = it.map { (_, c) -> c }.joinToString("")
          val idx = it.first().index
          word to idx
        }

    fun String.wordsInRow(rowIndex: Int): List<PlacedWord> =
      wordsWithIndex()
        .map { (word, idx) ->
          PlacedWord(word, Point(topLeftX + idx, topLeftY + rowIndex), Direction.Horizontal)
        }

    fun String.wordsInColumn(columnIndex: Int): List<PlacedWord> =
      wordsWithIndex()
        .map { (word, idx) ->
          PlacedWord(word, Point(topLeftX + columnIndex, topLeftY + idx), Direction.Vertical)
        }

    return buildList {
      lines().forEachIndexed { rowIndex, row -> addAll(row.wordsInRow(rowIndex)) }
      columns().forEachIndexed { columnIndex, column -> addAll(column.wordsInColumn(columnIndex)) }
    }
  }

  inline fun clearCells(cellsToRemove: Iterable<Point>) = cellsToRemove.forEach { cells.remove(it) }

  inline fun place(letter: Char, cell: Point) = place("$letter", cell, Direction.Horizontal)

  inline fun place(word: String, startCell: Point, dir: Direction): List<Point> =
    buildList {
      word.forEachIndexed { idx, letter ->
        val cell = startCell.moveBy(dir, idx)
        if (cell !in cells) {
          cells[cell] = letter
          add(cell)
        }
      }
    }

  inline fun canPlace(word: String, startCell: Point, dir: Direction): Boolean =
    canMergeWithoutConflict(word, startCell, dir) && canPlaceNewLetter(word, startCell, dir)

  inline fun canMergeWithoutConflict(word: String, startCell: Point, dir: Direction): Boolean =
    word.withIndex().all { (idx, letter) ->
      (cells[startCell.moveBy(dir, idx)] ?: letter) == letter
    }

  inline fun canPlaceNewLetter(word: String, startCell: Point, dir: Direction): Boolean =
    word.indices.any { idx ->
      startCell.moveBy(dir, idx) !in cells
    }

  inline fun isValid(
    dictionary: Dictionary,
    bagOfInputLetters: Bag<Char>,
    allowTouchingWords: Boolean,
    allowDuplicateWords: Boolean,
  ): Boolean =
    allWordsAreValid(dictionary)
      && allLettersUsedExactlyOnce(bagOfInputLetters)
      && (allowTouchingWords || noTouchingWords())
      && (allowDuplicateWords || noDuplicateWords())

  inline fun allLettersUsedExactlyOnce(bagOfInputLetters: Bag<Char>): Boolean =
    bagOfInputLetters == letters()

  inline fun allWordsAreValid(dictionary: Dictionary): Boolean =
    words(3).all { it.word in dictionary }

  inline fun noTouchingWords(): Boolean = words(2).none { it.word.length == 2 }

  inline fun noDuplicateWords(): Boolean = words(3).map { it.word }.isDistinct()

  inline fun getConnectedCells(point: Point, minWordLength: Int): Set<Point> =
    words(minWordLength)
      .filter { point in it.cells }
      .flatMap { it.cells }
      .toSet()

  inline fun getConnectedWords(point: Point, minWordLength: Int): Set<String> =
    words(minWordLength)
      .filter { point in it.cells }
      .map { it.word }
      .toSet()
}
