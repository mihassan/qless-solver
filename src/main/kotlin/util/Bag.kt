package util

@Suppress("NOTHING_TO_INLINE")
value class Bag<T>(
  val entries: MutableMap<T, Int> = mutableMapOf<T, Int>().withDefault { 0 },
) {
  init {
    require(entries.all { (_, c) -> c > 0 })
  }

  inline fun isEmpty(): Boolean = entries.isEmpty()

  inline operator fun contains(entry: T): Boolean = entries.getValue(entry) > 0

  inline operator fun plus(entry: T): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag += entry
    return newBag
  }

  inline operator fun plus(other: Bag<T>): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag += other
    return newBag
  }

  inline operator fun minus(other: Bag<T>): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag -= other
    return newBag
  }

  inline operator fun minus(entry: T): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag -= entry
    return newBag
  }

  inline operator fun plusAssign(entry: T) {
    entries[entry] = entries.getValue(entry) + 1
  }

  inline operator fun plusAssign(other: Bag<T>) {
    other.entries.forEach { (t, c) ->
      require(c > 0)
      entries[t] = entries.getValue(t) + c
    }
  }

  inline operator fun minusAssign(key: T) {
    val newValue = entries.getValue(key) - 1
    if (newValue > 0) {
      entries[key] = newValue
    } else {
      entries.remove(key)
    }
  }

  inline operator fun minusAssign(other: Bag<T>) {
    other.entries.forEach { (t, c) ->
      val newValue = entries.getValue(t) - c
      if (newValue > 0) {
        entries[t] = newValue
      } else {
        entries.remove(t)
      }
    }
  }

  companion object {
    inline fun <T> of(entries: Map<T, Int>): Bag<T> = Bag(entries.toMutableMap().withDefault { 0 })
  }
}
