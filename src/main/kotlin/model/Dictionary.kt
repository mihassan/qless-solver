package model

import util.words

data class Dictionary private constructor(val words: List<Word>) {

    operator fun contains(word: Word): Boolean = word in words
    operator fun contains(word: String): Boolean = Word.from(word) in words

    fun prune(allowedLetters: List<Char>): Dictionary = Dictionary(words.filter { it.canConstructFrom(allowedLetters) })

    companion object {
        fun emptyDictionary(): Dictionary = Dictionary(emptyList())

        fun from(words: List<String>): Dictionary = Dictionary(words.map { Word.from(it) }.filter { !it.isEmpty() })

        fun from(str: String): Dictionary = from(str.lines().flatMap { it.words() })
    }
}
