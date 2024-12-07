package syh.year2022.day14

data class Coord(val x: Int, val y: Int)
data class Wall(val coords: MutableList<Coord> = mutableListOf())

const val EMPTY = "."
const val WALL = "#"
const val SAND = "o"
const val SAND_ORIGIN = "+"

fun parseWall(line: String): Wall {
    val coords = line.split(" -> ").map {
        val (y, x) = it.split(",").map { itt -> itt.toInt() }
        Coord(x, y)
    }.toMutableList()
    return Wall(coords)
}
