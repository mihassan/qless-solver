package model

import util.frequency

data class Word private constructor(val letters: String) {
    private val lettersCount by lazy { letters.frequency() }

    fun isEmpty(): Boolean = letters.isEmpty()

    fun contains(letter: Char) = letter in letters

    fun canConstructFrom(bagOfLetters: List<Char>): Boolean {
        val bagOfLettersCount = bagOfLetters.frequency()
        return lettersCount.all { (l, c) ->
            bagOfLettersCount.getValue(l) >= c
        }
    }

    companion object {
        fun from(letters: String): Word = Word(letters.filter { it.isLetter() }.uppercase())
    }
}
