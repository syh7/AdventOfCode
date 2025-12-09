package syh.year2025

import kotlin.math.max
import kotlin.math.min
import syh.library.AbstractAocDay
import syh.library.Coord

class Puzzle9 : AbstractAocDay(2025, 9) {
    override fun doA(file: String): String {
        val coords = readSingleLineFile(file).map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Coord(x, y)
        }
        var biggestRectangle = 0L

        for (i in coords.indices) {
            for (j in i + 1..<coords.size) {
                val cornerA = coords[i]
                val cornerB = coords[j]
                if (cornerA.column == cornerB.column || cornerA.row == cornerB.row) {
                    println("skipping ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                    continue
                }

                val rectangle = calculateRectangle(cornerA, cornerB)
                if (rectangle > biggestRectangle) {
//                    println("calculated new biggest rectangle $rectangle for ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                    biggestRectangle = rectangle
                } else {
//                    println("calculated $rectangle for ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                }
            }
        }
        return biggestRectangle.toString()
    }

    override fun doB(file: String): String {
        val coords = readSingleLineFile(file).map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Coord(x, y)
        }

        val lastWall = coords.last() to coords.first()
        val walls = coords.windowed(2).map { it[0] to it[1] } + lastWall
        var biggestRectangle = 0L

        for (i in coords.indices) {
            for (j in i + 1..<coords.size) {
                val cornerA = coords[i]
                val cornerB = coords[j]
                if (cornerA.column == cornerB.column || cornerA.row == cornerB.row) {
//                    println("skipping ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                    continue
                }

                val rectangle = calculateRectangle(cornerA, cornerB)
                if (rectangle > biggestRectangle) {

                    // check if it breaks any walls
                    val doesNotIntersectWalls = doesNotIntersectWalls(walls, cornerA, cornerB)

                    if (doesNotIntersectWalls) {
                        println("calculated new biggest rectangle $rectangle for ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                        biggestRectangle = rectangle
                    } else {
//                        println("rectangle $rectangle breaks for ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                    }
                } else {
//                    println("calculated $rectangle for ${cornerA.toCoordString()} and ${cornerB.toCoordString()}")
                }
            }
        }

        return biggestRectangle.toString()
    }

    private fun doesNotIntersectWalls(walls: List<Pair<Coord, Coord>>, cornerA: Coord, cornerB: Coord): Boolean {

        walls.forEach { (firstWall, secondWall) ->
//            println("checking wall $firstWall->$secondWall")
            val maxRectRow = maxOf(cornerA.row, cornerB.row)
            val minRectRow = minOf(cornerA.row, cornerB.row)
            val maxRectColumn = maxOf(cornerA.column, cornerB.column)
            val minRectColumn = minOf(cornerA.column, cornerB.column)

            val maxWallRow = maxOf(firstWall.row, secondWall.row)
            val minWallRow = minOf(firstWall.row, secondWall.row)
            val maxWallColumn = maxOf(firstWall.column, secondWall.column)
            val minWallColumn = minOf(firstWall.column, secondWall.column)

            val intersect = if (maxWallRow == minWallRow) {
                // horizontal wall, check if wall row crosses rectangle row, if so, the wall column need to be outside the rectangle
                maxWallRow in (minRectRow + 1)..<maxRectRow && max(minRectColumn, minWallColumn) < min(maxRectColumn, maxWallColumn)
            } else {
                // vertical wall, check if wall column crosses rectangle column, if so, the wall row needs to be outside the rectangle
                maxWallColumn in (minRectColumn + 1)..<maxRectColumn && max(minRectRow, minWallRow) < min(maxRectRow, maxWallRow)
            }

            if (intersect) {
//                println("wall: $firstWall->$secondWall")
                return false
            }
        }
        return true
    }

    private fun calculateRectangle(cornerA: Coord, cornerB: Coord): Long {
        val rowSize = maxOf(cornerA.row, cornerB.row) - minOf(cornerA.row, cornerB.row) + 1
        val columnSize = maxOf(cornerA.column, cornerB.column) - minOf(cornerA.column, cornerB.column) + 1
        return rowSize.toLong() * columnSize.toLong()
    }
}