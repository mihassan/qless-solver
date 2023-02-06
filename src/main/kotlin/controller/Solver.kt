package controller

import model.*
import util.remove

class Solver(private val dictionary: Dictionary) {
    fun solve(inputLetters: List<Char>): Board? {
        val globalDictionary = dictionary.prune(inputLetters)

        fun solveInternal(localDictionary: Dictionary, inputLetters: List<Char>, partialBoard: Board): Board? {
            if (partialBoard.containsExactly(inputLetters)) {
                return partialBoard.takeIf { partialBoard.isValid(globalDictionary, inputLetters) }
            }

            return partialBoard.cells().firstNotNullOfOrNull { (commonCell, requiredLetter) ->
                val remainingLetters = inputLetters.remove(partialBoard.letters())
                val allowedLetters = remainingLetters + requiredLetter
                val prunedDictionary = localDictionary.prune(allowedLetters)

                prunedDictionary.words
                    .filter { word -> requiredLetter in word.letters }
                    .firstNotNullOfOrNull { word ->
                        partialBoard.possiblePlacements(commonCell, word.letters)
                            .firstNotNullOfOrNull { (startCell, dir) ->
                                val nextBoard = partialBoard.place(word.letters, startCell, dir)
                                solveInternal(prunedDictionary, inputLetters, nextBoard)
                            }
                    }
            }
        }

        return globalDictionary.words.firstNotNullOfOrNull { word ->
            val boardWithFirstWord = Board.from(word.letters)
            solveInternal(globalDictionary, inputLetters, boardWithFirstWord)
        }
    }

    companion object {
        private fun Board.possiblePlacements(cell: Point, word: String): List<Pair<Point, Direction>> =
            Direction.values().flatMap { dir ->
                word.indices.mapNotNull { offset ->
                    val startCell = cell - dir * offset
                    val newBoard = Board.from(word, startCell, dir)
                    if (canMergeWithoutConflict(newBoard) && !containsEntire(newBoard)) startCell to dir else null
                }
            }

        private fun Board.isValid(dictionary: Dictionary, inputLetters: List<Char>): Boolean =
            words().filter { it.length >= 2 }.all { it in dictionary } && containsExactly(inputLetters)
    }
}
