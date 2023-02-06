package model

enum class Direction(internal val vector: Point) {
    Horizontal(Point(1, 0)),
    Vertical(Point(0, 1));

    operator fun times(scale: Int): Point = vector * scale
    operator fun div(scale: Int): Point = vector / scale
}
