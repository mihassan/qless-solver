package model

import util.transpose
import util.words

data class Board private constructor(private val cells: Map<Point, Char> = mapOf()) {
    fun cells(): List<Pair<Point, Char>> = cells.entries.map { it.toPair() }

    fun letters(): List<Char> = cells.values.toList()

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

    fun place(word: String, cell: Point, dir: Direction): Board {
        return Board(buildMap {
            putAll(cells)
            word.forEachIndexed { idx, letter ->
                put(cell + dir * idx, letter)
            }
        })
    }

    fun canMergeWithoutConflict(other: Board): Boolean =
        (cells.keys intersect other.cells.keys).all { cells[it] == other.cells[it] }

    fun containsEntire(other: Board): Boolean = cells.keys.containsAll(other.cells.keys)

    fun containsExactly(letters: List<Char>) = letters().sorted() == letters.sorted()

    companion object {
        fun from(word: String, cell: Point = Point(0, 0), dir: Direction = Direction.Horizontal): Board =
            Board().place(word, cell, dir)
    }
}
