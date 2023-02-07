@file:Suppress("NOTHING_TO_INLINE")

package util

inline fun <T> List<T>.frequency(): Bag<T> = Bag.of(groupingBy { it }.eachCount())

inline fun CharArray.frequency(): Bag<Char> = toList().frequency()
inline fun String.frequency(): Bag<Char> = toCharArray().frequency()

inline fun <T> List<List<T>>.transpose(): List<List<T>> {
  val rowCount = size
  val colCount = first().size
  return List(colCount) { y ->
    List(rowCount) { x ->
      this[x][y]
    }
  }
}

inline fun List<String>.transpose(): List<String> =
  map { it.toCharArray().toList() }.transpose().map { it.joinToString("") }

inline fun String.words() = split(" ")
