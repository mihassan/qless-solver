package model

import util.Bag
import util.frequency

@Suppress("DataClassPrivateConstructor")
data class Word private constructor(val letters: String) {
  val lettersCount: Bag<Char> by lazy { letters.frequency() }

  fun contains(letter: Char) = letter in lettersCount

  fun canConstructFrom(bagOfInputLetters: Bag<Char>): Boolean =
    (lettersCount - bagOfInputLetters).isEmpty()

  companion object {
    fun from(letters: String): Word? =
      Word(
        letters
          .filter { it.isLetter() }
          .uppercase()
      ).takeIf {
        it.letters.isNotEmpty()
      }
  }
}
