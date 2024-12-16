package syh.library

data class Coord(val row: Int, val column: Int) {

    fun toCoordString() = "[$row][$column]"

    fun relative(direction: Direction): Coord {
        return Coord(this.row + direction.row, this.column + direction.col)
    }

}