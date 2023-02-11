package model

import util.Bag
import util.frequency

@Suppress("DataClassPrivateConstructor")
data class Word private constructor(val letters: String) {
  private val bagOfLetters: Bag<Char> by lazy { letters.frequency() }

  fun contains(letter: Char) = letter in bagOfLetters

  fun canConstructFrom(bagOfInputLetters: Bag<Char>): Boolean =
    bagOfLetters isSubSetOf bagOfInputLetters

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
