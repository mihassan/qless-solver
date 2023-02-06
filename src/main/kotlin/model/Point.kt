package model

data class Point(val x: Int, val y: Int) {
    operator fun times(scale: Int): Point = Point(x * scale, y * scale)
    operator fun div(scale: Int): Point = Point(x / scale, y / scale)

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
    operator fun plus(direction: Direction): Point = this + direction.vector

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)
    operator fun minus(direction: Direction): Point = this - direction.vector

    operator fun unaryPlus(): Point = this
    operator fun unaryMinus(): Point = Point(-x, -y)
}
