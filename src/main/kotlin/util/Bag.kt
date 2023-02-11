package util

@Suppress("NOTHING_TO_INLINE")
value class Bag<T>(
  val entries: MutableMap<T, Int> = mutableMapOf<T, Int>().withDefault { 0 },
) {
  init {
    require(entries.all { (_, c) -> c > 0 })
  }

  inline fun isEmpty(): Boolean = entries.isEmpty()

  inline val size: Int get() = entries.values.sum()

  operator fun get(key: T): Int = entries.getValue(key)

  operator fun set(key: T, count: Int) {
    entries[key] = count
  }

  inline operator fun contains(key: T): Boolean = this[key] > 0

  inline infix fun isSubSetOf(other: Bag<T>): Boolean =
    entries.all { (t, c) -> c <= other[t] }

  inline operator fun plus(key: T): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag += key
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

  inline operator fun minus(key: T): Bag<T> {
    val newBag = Bag<T>()
    newBag += this
    newBag -= key
    return newBag
  }

  inline operator fun plusAssign(key: T) {
    this[key] += 1
  }

  inline operator fun plusAssign(other: Bag<T>) {
    other.entries.forEach { (t, c) ->
      require(c > 0)
      this[t] += c
    }
  }

  inline operator fun minusAssign(key: T) {
    val newValue = this[key] - 1
    if (newValue > 0) {
      this[key] = newValue
    } else {
      entries.remove(key)
    }
  }

  inline operator fun minusAssign(other: Bag<T>) {
    other.entries.forEach { (t, c) ->
      val newValue = this[t] - c
      if (newValue > 0) {
        this[t] = newValue
      } else {
        entries.remove(t)
      }
    }
  }

  companion object {
    inline fun <T> of(entries: Map<T, Int>): Bag<T> = Bag(entries.toMutableMap().withDefault { 0 })
  }
}
