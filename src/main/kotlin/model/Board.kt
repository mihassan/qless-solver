package model

import util.Bag
import util.frequency
import util.transpose
import util.words

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

@Suppress("NOTHING_TO_INLINE")
value class Board(val cells: MutableMap<Point, Char> = mutableMapOf()) {
  inline fun clone(): Board = Board(cells.toMutableMap())

  inline fun letters(): Bag<Char> = cells.values.toList().frequency()

  inline fun xRange(): IntRange = cells.keys.map(Point::x).run { min()..max() }

  inline fun yRange(): IntRange = cells.keys.map(Point::y).run { min()..max() }

  inline fun show(): String = yRange().joinToString("\n") { y ->
    xRange().map { x ->
      cells[Point(x, y)] ?: ' '
    }.joinToString("")
  }

  inline fun lines(): List<String> = show().lines()

  inline fun columns(): List<String> = lines().transpose()

  inline fun words(): List<String> = (lines() + columns()).flatMap(String::words)

  inline fun clear() = cells.clear()

  inline fun clearCells(cellsToRemove: Iterable<Point>) = cellsToRemove.forEach { cells.remove(it) }

  inline fun place(word: String, startCell: Point, dir: Direction): List<Pair<Point, Char>> = buildList {
    word.forEachIndexed { idx, letter ->
      val cell = startCell.moveBy(dir, idx)
      if (cell !in cells) {
        cells[cell] = letter
        add(cell to letter)
      }
    }
  }

  inline fun possiblePlacements(word: String, cell: Point): List<Pair<Point, Direction>> =
    Direction.values().flatMap { dir ->
      word.indices.map { idx ->
        cell.moveBy(dir, -idx) to dir
      }.filter { (startCell, dir) ->
        canMergeWithoutConflict(word, startCell, dir) && canPlaceNewLetter(word, startCell, dir)
      }
    }

  inline fun canMergeWithoutConflict(word: String, startCell: Point, dir: Direction): Boolean =
    word.withIndex().all { (idx, letter) ->
      (cells[startCell.moveBy(dir, idx)] ?: letter) == letter
    }

  inline fun canPlaceNewLetter(word: String, startCell: Point, dir: Direction): Boolean =
    word.indices.any { idx ->
      startCell.moveBy(dir, idx) !in cells
    }

  inline fun isValid(dictionary: Dictionary, bagOfInputLetters: Bag<Char>): Boolean =
    allWordsAreValid(dictionary) && allInputLettersAreUsedExactlyOnce(bagOfInputLetters)

  inline fun allInputLettersAreUsedExactlyOnce(bagOfInputLetters: Bag<Char>): Boolean =
    bagOfInputLetters == letters()

  inline fun allWordsAreValid(dictionary: Dictionary): Boolean =
    words().filter { it.length >= 2 }.all { it in dictionary }
}
