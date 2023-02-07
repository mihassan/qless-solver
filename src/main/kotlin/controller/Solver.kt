package controller

import model.*
import util.Bag
import util.frequency

class Solver(private val dictionary: Dictionary) {
  fun solve(inputLetters: List<Char>): Board? {
    val bagOfInputLetters = inputLetters.frequency()
    val dictionary = dictionary.prune(bagOfInputLetters)
    val board = Board()

    fun solveInternal(remainingLetters: Bag<Char>): Board? {
      if (remainingLetters.isEmpty()) {
        return if (board.isValid(dictionary, bagOfInputLetters)) {
          board.clone()
        } else {
          null
        }
      }

      return board.cells.firstNotNullOfOrNull { (commonCell, requiredLetter) ->
        console.log("Start 3: Trying $commonCell $requiredLetter\n${board.show()}")
        remainingLetters += requiredLetter

        val result = dictionary.entries.asSequence()
          .filter { word ->
            word.canConstructFrom(remainingLetters) && requiredLetter in word.lettersCount
          }.firstNotNullOfOrNull { word ->
            console.log("Start 4: Trying $word")
            remainingLetters -= word.lettersCount

            val result = board.possiblePlacements(word.letters, commonCell)
              .firstNotNullOfOrNull { (startCell, dir) ->
                console.log("Start 5: Trying $startCell $dir")
                val newEntries = board.place(word.letters, startCell, dir)
                val newCells = newEntries.map { it.first }
                val newLetters = newEntries.map { it.second }.frequency()

                val result = solveInternal(remainingLetters - newLetters)

                board.clearCells(newCells)
                console.log("End 5: Trying $startCell $dir")
                result
              }

            remainingLetters += word.lettersCount
            console.log("End 4: Trying $word}")
            result
          }

        remainingLetters -= requiredLetter
        console.log("End 3: Trying $commonCell $requiredLetter\n${board.show()}")
        result
      }
    }

    return dictionary.entries.firstNotNullOfOrNull { entry ->
      board.clear()
      board.place(entry.letters, Point(0, 0), Direction.Horizontal)
      solveInternal(bagOfInputLetters - entry.lettersCount)
    }
  }
}
