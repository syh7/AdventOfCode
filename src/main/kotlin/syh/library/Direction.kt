package syh.library

data class Direction(val row: Int, val col: Int) {

    fun left90(): Direction {
        return Direction(-1 * row, col)
    }

    fun right90(): Direction {
        return Direction(row, -1 * col)
    }

    fun opposite(): Direction {
        return Direction(row * -1, col * -1)
    }

    override fun toString(): String {
        return "($row,$col)"
    }

    companion object {
        val NORTH: Direction = Direction(-1, 0)
        val EAST: Direction = Direction(0, 1)
        val SOUTH: Direction = Direction(1, 0)
        val WEST: Direction = Direction(0, -1)
        val NORTH_EAST: Direction = Direction(-1, 1)
        val SOUTH_EAST: Direction = Direction(1, 1)
        val SOUTH_WEST: Direction = Direction(1, -1)
        val NORTH_WEST: Direction = Direction(-1, -1)

        val ALL_DIRECTIONS: List<Direction> = listOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)
        val CARDINAL_DIRECTIONS: List<Direction> = listOf(NORTH, EAST, SOUTH, WEST)
        val ORDINAL_DIRECTIONS: List<Direction> = listOf(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST)
    }
}