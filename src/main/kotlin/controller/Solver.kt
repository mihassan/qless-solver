package controller

import model.*
import util.Bag
import util.frequency
import util.memoize

class Solver(private val dictionary: Dictionary) {
  fun solve(inputLetters: List<Char>): Board? {
    val bagOfInputLetters = inputLetters.frequency()
    val dictionary = dictionary.prune(bagOfInputLetters)
    val prune: (Bag<Char>) -> Dictionary = memoize { dictionary.prune(it) }
    val board = Board()

    fun candidates(depth: Int, requiredLetter: Char): Dictionary {
      val allowedLetters = when (depth) {
        1 -> bagOfInputLetters
        else -> bagOfInputLetters - board.letters() + requiredLetter
      }
      return prune(allowedLetters)
    }

    fun solveInternal(depth: Int): Board? {
      if (board.allInputLettersAreUsedExactlyOnce(bagOfInputLetters)) {
        return if (board.isValid(dictionary, bagOfInputLetters)) board.clone() else null
      }
      return board.cells.firstNotNullOfOrNull { (commonCell, requiredLetter) ->
        candidates(depth, requiredLetter).entries.firstNotNullOfOrNull { entry ->
          Direction.values().firstNotNullOfOrNull { dir ->
            entry.letters.withIndex().firstNotNullOfOrNull { (offset, letter) ->
              // console.log("${board.show()}")
              val startCell = commonCell.moveBy(dir, -offset)
              if (letter == requiredLetter && board.canPlace(entry.letters, startCell, dir)) {
                val newCells = board.place(entry.letters, startCell, dir)
                val result = solveInternal(depth + 1)
                board.clearCells(newCells)
                result
              } else {
                null
              }
            }
          }
        }
      }
    }

    board.place(findLeastFrequentLetter(dictionary), Point(0, 0))
    return solveInternal(1)
  }

  private companion object {
    fun findLeastFrequentLetter(dictionary: Dictionary) =
      dictionary
        .words
        .joinToString("")
        .frequency()
        .entries
        .minBy { it.value }
        .key
  }
}
