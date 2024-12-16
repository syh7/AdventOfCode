package syh.library

class Direction(val row: Int, val col: Int) {

    fun left90(): Direction {
        return Direction(-1 * col, row)
    }

    fun right90(): Direction {
        return Direction(col, -1 * row)
    }

    override fun toString(): String {
        return "($row,$col)"
    }

    companion object {
        val N: Direction = Direction(-1, 0)
        val E: Direction = Direction(0, 1)
        val S: Direction = Direction(1, 0)
        val W: Direction = Direction(0, -1)
        val NE: Direction = Direction(-1, 1)
        val SE: Direction = Direction(1, 1)
        val SW: Direction = Direction(1, -1)
        val NW: Direction = Direction(-1, -1)

        val ALL_DIRECTIONS: List<Direction> = listOf(N, NE, E, SE, S, SW, W, NW)
        val CARDINAL_DIRECTIONS: List<Direction> = listOf(N, E, S, W)
        val ORDINAL_DIRECTIONS: List<Direction> = listOf(NE, SE, SW, NW)
    }
}