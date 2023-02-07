package controller

import model.*
import util.Bag
import util.frequency

class Solver(private val dictionary: Dictionary) {
  fun solve(inputLetters: List<Char>): Board? {
    val bagOfInputLetters = inputLetters.frequency()
    val dictionary = dictionary.prune(bagOfInputLetters)
    val board = Board()

    console.log("Dictionary ${dictionary.size}")
    val df = dictionary.entries.map { it.letters.length }.frequency()
    console.log("Dictionary $df")
    console.log("Dictionary $dictionary")

    fun solveInternal(remainingLetters: Bag<Char>): Board? {
      console.log("Start 2: Trying $remainingLetters\n${board.show()}")
      if (remainingLetters.isEmpty()) {
        return if (board.isValid(dictionary, bagOfInputLetters)) {
          board.clone()
        } else {
          null
        }
      }

      val result = board.cells.firstNotNullOfOrNull { (commonCell, requiredLetter) ->
        console.log("Start 3: Trying $commonCell $requiredLetter $remainingLetters\n${board.show()}")
        remainingLetters += requiredLetter

        val result = dictionary.entries.asSequence()
          .filter { word ->
            word.canConstructFrom(remainingLetters) && requiredLetter in word.lettersCount
          }.firstNotNullOfOrNull { word ->
            console.log("Start 4: Trying $word $remainingLetters")

            val result = board.possiblePlacements(word.letters, commonCell)
              .firstNotNullOfOrNull { (startCell, dir) ->
                console.log("Start 5: Trying $startCell $dir $remainingLetters")
                val newEntries = board.place(word.letters, startCell, dir)
                val newCells = newEntries.map { it.first }
                val newLetters = newEntries.map { it.second }.frequency()

                val result = solveInternal(remainingLetters - newLetters - requiredLetter)

                board.clearCells(newCells)
                console.log("End 5: Trying $startCell $dir $remainingLetters")
                result
              }

            console.log("End 4: Trying $word $remainingLetters")
            result
          }

        remainingLetters -= requiredLetter
        console.log("End 3: Trying $commonCell $requiredLetter $remainingLetters\n${board.show()}")
        result
      }
      console.log("End 2: Trying $remainingLetters\n${board.show()}")
      return result
    }

    return dictionary.entries.firstNotNullOfOrNull { entry ->
      board.clear()
      board.place(entry.letters, Point(0, 0), Direction.Horizontal)
      console.log("Start 1: Trying $entry\n${board.show()}")
      val result = solveInternal(bagOfInputLetters - entry.lettersCount)
      console.log("End 1: Trying $entry\n${board.show()}")
      result
    }
  }
}
