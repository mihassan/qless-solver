package util

fun <I, O> memoize(fn: (I) -> O): (I) -> O {
  val cache = mutableMapOf<I, O>()
  return {
    cache.getOrPut(it) { fn(it) }
  }
}
