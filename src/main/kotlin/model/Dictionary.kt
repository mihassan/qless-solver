package model

import util.Bag
import util.frequency
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

  private val letterFrequency by lazy { words.joinToString("").frequency() }

  operator fun contains(word: Word): Boolean = word in entries

  operator fun contains(word: String): Boolean = word in words

  fun sort(): Dictionary {
    val sortedEntries = entries.sortedByDescending { it.letters.length }
    return Dictionary(sortedEntries.map { it.letters }.toSet(), sortedEntries.toSet())
  }

  fun prune(bagOfLetters: Bag<Char>): Dictionary {
    val prunedEntries =
      entries.filter { it.canConstructFrom(bagOfLetters) && it.letters.length >= 3 }
    return Dictionary(prunedEntries.map { it.letters }.toSet(), prunedEntries.toSet())
  }

  fun findLeastFrequentLetter(): Char? = letterFrequency.entries.minByOrNull { it.value }?.key

  companion object {
    private val SPACE = Regex("""\s""")

    fun of(words: Collection<String>): Dictionary {
      val entries = words.mapNotNull { Word.from(it) }
      return Dictionary(entries.map { it.letters }.toSet(), entries.toSet())
    }

    fun of(dictionary: String): Dictionary =
      of(dictionary.lines().filterNot { SPACE in it }.flatMap { it.words() })
  }
}
