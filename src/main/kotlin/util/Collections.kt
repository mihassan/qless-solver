@file:Suppress("NOTHING_TO_INLINE")

package util

inline fun <T> List<T>.frequency(): Bag<T> = Bag.of(groupingBy { it }.eachCount())
inline fun CharArray.frequency(): Bag<Char> = toList().frequency()
inline fun String.frequency(): Bag<Char> = toCharArray().frequency()

inline fun <T> List<T>.isDistinct(): Boolean = size == distinct().size

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

fun <T> List<T>.powerSet(): List<List<T>> =
  if (isEmpty()) listOf(emptyList())
  else {
    val powerSetOfRest: List<List<T>> = drop(1).powerSet()
    powerSetOfRest + powerSetOfRest.map { it + first() }
  }

fun <T> List<T>.groupContiguousBy(predicate: (List<T>, T) -> Boolean): List<List<T>> {
  val groups = mutableListOf<MutableList<T>>()
  forEach { e ->
    val lastGroup = groups.lastOrNull()
    if (lastGroup != null && predicate(lastGroup, e)) {
      lastGroup.add(e)
    } else {
      groups.add(mutableListOf(e))
    }
  }
  return groups
}

fun <T> List<T>.groupContiguousBy(fn: (T) -> Boolean): List<List<T>> =
  groupContiguousBy { ts, t -> ts.lastOrNull()?.let(fn) == fn(t) }

inline fun String.words() = split(" ")
