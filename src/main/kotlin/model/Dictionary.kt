package model

import util.Bag
import util.frequency

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

  private fun updateEntries(fn: Set<Word>.() -> List<Word>): Dictionary = with(fn(entries)) {
    Dictionary(map { it.letters }.toSet(), toSet())
  }

  fun sortAlphabeticOrder(): Dictionary = updateEntries { sortedBy { it.letters } }

  fun sortShortestFirst(): Dictionary = updateEntries { sortedBy { it.letters.length } }

  fun sortLongestFirst(): Dictionary = updateEntries { sortedByDescending { it.letters.length } }

  fun shuffle(): Dictionary = updateEntries { shuffled() }

  fun prune(bagOfLetters: Bag<Char>, bannedWords: Set<String>): Dictionary {
    val prunedEntries = entries.filter {
      it.canConstructFrom(bagOfLetters) && it.letters !in bannedWords && it.letters.length >= 3
    }
    return Dictionary(prunedEntries.map { it.letters }.toSet(), prunedEntries.toSet())
  }

  fun findLeastFrequentLetter(): Char? = letterFrequency.entries.minByOrNull { it.value }?.key

  companion object {
    private val SPACE = Regex("""\s""")

    fun of(words: Collection<String>): Dictionary {
      val entries = words.mapNotNull { Word.from(it) }
      return Dictionary(entries.map { it.letters }.toSet(), entries.toSet())
    }

    fun of(dictionary: String): Dictionary = of(dictionary.lines().filterNot { SPACE in it })
  }
}
