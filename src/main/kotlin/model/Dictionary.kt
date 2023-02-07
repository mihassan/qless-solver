package model

import util.Bag
import util.words

@Suppress("DataClassPrivateConstructor")
data class Dictionary private constructor(
  val words: Set<String>,
  val entries: Set<Word>,
) {
  init {
    require(words.size == entries.size)
  }
  
  val size: Int = words.size

  operator fun contains(word: Word): Boolean = word in entries

  operator fun contains(word: String): Boolean = word in words

  fun prune(bagOfLetters: Bag<Char>): Dictionary {
    val prunedEntries = entries.filter { it.canConstructFrom(bagOfLetters) }
    return Dictionary(prunedEntries.map { it.letters }.toSet(), prunedEntries.toSet())
  }

  companion object {
    fun from(words: Collection<String>): Dictionary {
      val entries = words.mapNotNull { Word.from(it) }
      return Dictionary(entries.map { it.letters }.toSet(), entries.toSet())
    }

    fun from(dictionary: String): Dictionary = from(dictionary.lines().flatMap { it.words() })
  }
}
