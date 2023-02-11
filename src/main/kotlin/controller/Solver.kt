package controller

import model.*
import util.Bag
import util.frequency

class Solver(private val dictionary: Dictionary) {
  fun solve(inputLetters: List<Char>): Board? {
    val bagOfInputLetters = inputLetters.frequency()
    val dictionary = dictionary.prune(bagOfInputLetters)
    val board = Board()

    val prunedDictionaryCache = mutableMapOf<Bag<Char>, Dictionary>()
    fun pruneDictionary(bagOfLetters: Bag<Char>): Dictionary =
      prunedDictionaryCache
        .getOrPut(bagOfLetters) {
          dictionary.prune(bagOfLetters)
        }

    inline fun allowedLetters(depth: Int, requiredLetter: Char) = when (depth) {
      1 -> bagOfInputLetters
      else -> bagOfInputLetters - board.letters() + requiredLetter
    }

    inline fun candidates(depth: Int, requiredLetter: Char): Dictionary =
      pruneDictionary(allowedLetters(depth, requiredLetter))

    fun solveInternal(depth: Int): Board? =
      if (board.allLettersUsedExactlyOnce(bagOfInputLetters)) {
        if (board.isValid(dictionary, bagOfInputLetters)) {
          board.clone()
        } else {
          null
        }
      } else {
        board.cells.firstNotNullOfOrNull { (commonCell, requiredLetter) ->
          candidates(depth, requiredLetter).entries.firstNotNullOfOrNull { entry ->
            Direction.values().firstNotNullOfOrNull { dir ->
              entry.letters.withIndex().firstNotNullOfOrNull { (offset, letter) ->
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

    board.place(dictionary.findLeastFrequentLetter(), Point(0, 0))
    return solveInternal(1)
  }
}
