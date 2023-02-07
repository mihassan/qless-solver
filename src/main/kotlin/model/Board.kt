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

value class Board(val cells: MutableMap<Point, Char> = mutableMapOf()) {
  fun clone(): Board = Board(cells.toMutableMap())

  fun letters(): Bag<Char> = cells.values.toList().frequency()

  fun xRange(): IntRange = cells.keys.map(Point::x).run { min()..max() }

  fun yRange(): IntRange = cells.keys.map(Point::y).run { min()..max() }

  fun show(): String = yRange().joinToString("\n") { y ->
    xRange().map { x ->
      cells[Point(x, y)] ?: ' '
    }.joinToString("")
  }

  fun lines(): List<String> = show().lines()

  fun columns(): List<String> = lines().transpose()

  fun words(): List<String> = (lines() + columns()).flatMap(String::words)

  fun clear() = cells.clear()

  fun clearCells(cellsToRemove: Iterable<Point>) = cellsToRemove.forEach { cells.remove(it) }

  fun place(word: String, startCell: Point, dir: Direction): List<Pair<Point, Char>> = buildList {
    word.forEachIndexed { idx, letter ->
      val cell = startCell.moveBy(dir, idx)
      if (cell !in cells) {
        cells[cell] = letter
        add(cell to letter)
      }
    }
  }

  fun possiblePlacements(word: String, cell: Point): List<Pair<Point, Direction>> =
    Direction.values().flatMap { dir ->
      word.indices.map { idx ->
        cell.moveBy(dir, -idx) to dir
      }.filter { (startCell, dir) ->
        canMergeWithoutConflict(word, startCell, dir) && canPlaceNewLetter(word, startCell, dir)
      }
    }

  private fun canMergeWithoutConflict(word: String, startCell: Point, dir: Direction): Boolean =
    word.withIndex().all { (idx, letter) ->
      (cells[startCell.moveBy(dir, idx)] ?: letter) == letter
    }

  private fun canPlaceNewLetter(word: String, startCell: Point, dir: Direction): Boolean =
    word.indices.any { idx ->
      startCell.moveBy(dir, idx) !in cells
    }

  fun isValid(dictionary: Dictionary, bagOfInputLetters: Bag<Char>): Boolean =
    allWordsAreValid(dictionary) && allInputLettersAreUsedExactlyOnce(bagOfInputLetters)

  private fun allInputLettersAreUsedExactlyOnce(bagOfInputLetters: Bag<Char>): Boolean =
    bagOfInputLetters == letters()

  private fun allWordsAreValid(dictionary: Dictionary): Boolean =
    words().filter { it.length >= 2 }.all { it in dictionary }
}
