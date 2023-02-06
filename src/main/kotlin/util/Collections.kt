package util

fun <T> List<T>.frequency(): Map<T, Int> = groupingBy { it }.eachCount().withDefault { 0 }
fun CharArray.frequency(): Map<Char, Int> = toList().frequency()
fun String.frequency(): Map<Char, Int> = toCharArray().frequency()

fun <T> List<T>.remove(other: List<T>): List<T> = buildList {
    addAll(this@remove)
    other.forEach { remove(it) }
}

fun List<Char>.remove(other: CharArray): List<Char> = remove(other.toList())
fun List<Char>.remove(other: String): List<Char> = remove(other.toCharArray())

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val rowCount = size
    val colCount = first().size
    return List(colCount) { y ->
        List(rowCount) { x ->
            this[x][y]
        }
    }
}

fun List<String>.transpose(): List<String> =
    map { it.toCharArray().toList() }.transpose().map { it.joinToString("") }

fun String.words() = split(" ")
