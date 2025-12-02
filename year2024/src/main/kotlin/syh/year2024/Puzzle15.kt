package syh.year2024

import syh.library.AbstractAocDay

class Puzzle15 : AbstractAocDay(2024, 15) {
    override fun doA(file: String): String {
        val (gridLines, instructionsStr) = readDoubleLineFile(file).map { it.split("\r\n") }

        val grid = mutableListOf<MutableList<Coord>>()
        for (j in gridLines.indices) {
            val row = mutableListOf<Coord>()
            for (i in gridLines[j].indices) {
                row.add(Coord(i, j, gridLines[j][i].toString()))
            }
            grid.add(row)
        }

        printGrid(grid)

        val startCoord = grid.flatten().first { it.str == "@" }
        startCoord.str = "."
        var robot = Coord(startCoord.x, startCoord.y, "@")
        println("found start at ${robot.toCoordStr()}")


        val instructions = instructionsStr.joinToString("")


        for (instruction in instructions) {
            val (xOffset: Int, yOffset: Int) = readInstruction(instruction)

            val success = attemptMoveA(xOffset, yOffset, robot, grid)
            if (success) {
//                println("successful move")
                robot = Coord(robot.x + xOffset, robot.y + yOffset, robot.str)
            } else {
//                println("could not move $instruction")
            }

//            printGrid(grid)
            println()
        }


        var total = 0
        for (row in grid) {
            row.filter { it.str == "O" }.forEach {
                val gps = it.y * 100 + it.x
                println("box $it has value $gps")
                total += gps
            }
        }

        return total.toString()
    }

    override fun doB(file: String): String {
        val (gridLines, instructionsStr) = readDoubleLineFile(file).map { it.split("\r\n") }

        val grid = mutableListOf<MutableList<Coord>>()
        for (j in gridLines.indices) {
            var iOffset = 0
            val row = mutableListOf<Coord>()
            for (i in gridLines[j].indices) {
                if (gridLines[j][i].toString() == "O") {
                    row.add(Coord(i + iOffset, j, "["))
                    iOffset++
                    row.add(Coord(i + iOffset, j, "]"))
                } else if (gridLines[j][i].toString() == "@") {
                    row.add(Coord(i + iOffset, j, "@"))
                    iOffset++
                    row.add(Coord(i + iOffset, j, "."))
                } else {
                    row.add(Coord(i + iOffset, j, gridLines[j][i].toString()))
                    iOffset++
                    row.add(Coord(i + iOffset, j, gridLines[j][i].toString()))
                }
            }
            grid.add(row)
        }

        printGrid(grid)

        val startCoord = grid.flatten().first { it.str == "@" }
        startCoord.str = "."
        var robot = Coord(startCoord.x, startCoord.y, "@")
        println("found start at ${robot.toCoordStr()}")


        val instructions = instructionsStr.joinToString("")


        for (instruction in instructions) {
            val (xOffset, yOffset) = readInstruction(instruction)

            println("read instruction $instruction with start $robot")

            val success = attemptMoveB(xOffset, yOffset, robot, grid)
            if (success) {
//                println("can move boxes")
                doMoveB(xOffset, yOffset, robot, grid)
//                moveBoxes(boxes, xOffset, yOffset, grid)
                robot = Coord(robot.x + xOffset, robot.y + yOffset, robot.str)
            } else {
                println("could not move $instruction")
            }

//            printGrid(grid)
//            println()
        }


        var total = 0
        for (row in grid) {
            row.filter { it.str == "[" }.forEach {
                val gps = it.y * 100 + it.x
                println("box $it has value $gps")
                total += gps
            }
        }

        return total.toString()
    }

    private fun printGrid(grid: MutableList<MutableList<Coord>>) {
        println("grid:")
        for (row in grid) {
            println(row.joinToString("") { it.str })
        }
    }

    private fun attemptMoveA(xOffset: Int, yOffset: Int, start: Coord, grid: MutableList<MutableList<Coord>>): Boolean {
        val checkX = start.x + xOffset
        val checkY = start.y + yOffset

        if (checkX !in grid[0].indices || checkY !in grid.indices) return false
        val checkCoord = grid[checkY][checkX]

        if (checkCoord.str == ".") {
//            println("empty space")
            return true
        }

        if (checkCoord.str == "#") {
//            println("wall")
            return false
        }

        if (checkCoord.str == "O") {
            println("box")
            val moveBox = attemptMoveA(xOffset, yOffset, checkCoord, grid)
            if (moveBox) {
                println("successful move")
                checkCoord.str = "."
                val boxCoord = grid[checkCoord.y + yOffset][checkCoord.x + xOffset]
                boxCoord.str = "O"
            }
            return moveBox
        }

        throw IllegalStateException("checkCoord $checkCoord has an unknown value")
    }

    private fun attemptMoveB(xOffset: Int, yOffset: Int, start: Coord, grid: MutableList<MutableList<Coord>>): Boolean {
        val checkX = start.x + xOffset
        val checkY = start.y + yOffset

        if (checkX !in grid[0].indices || checkY !in grid.indices) {
            println("out of bounds [$checkX][$checkY]")
            return false
        }
        val checkCoord = grid[checkY][checkX]

        if (checkCoord.str == ".") {
            println("empty space")
            return true
        }

        if (checkCoord.str == "#") {
            println("wall")
            return false
        }

        if (xOffset != 0) {
            // move left or right
            var endOfBoxes = 1
            while (grid[start.y][start.x + xOffset * endOfBoxes].str in "[]") {
                endOfBoxes += 1
            }
            if (grid[start.y][start.x + xOffset * endOfBoxes].str == ".") {
                println("can move $endOfBoxes boxes")
                return true
            }
            return false

        } else {

            // moving up or down
            if (checkCoord.str == "[") {
                println("box left")
                if (checkX + 1 !in grid[0].indices || checkY !in grid.indices) return false
                val moveBox = attemptMoveB(xOffset, yOffset, checkCoord, grid) && attemptMoveB(xOffset, yOffset, grid[checkY][checkX + 1], grid)
                println("can move box: $moveBox")
                return moveBox
            }

            if (checkCoord.str == "]") {
                println("box right")
                if (checkX - 1 !in grid[0].indices || checkY !in grid.indices) return false
                val moveBox = attemptMoveB(xOffset, yOffset, checkCoord, grid) && attemptMoveB(xOffset, yOffset, grid[checkY][checkX - 1], grid)
                println("can move box: $moveBox")
                return moveBox
            }
        }

        throw IllegalStateException("checkCoord $checkCoord has an unknown value")
    }

    private fun doMoveB(xOffset: Int, yOffset: Int, start: Coord, grid: MutableList<MutableList<Coord>>) {
        val checkX = start.x + xOffset
        val checkY = start.y + yOffset

        val checkCoord = grid[checkY][checkX]

        if (checkCoord.str == "." || checkCoord.str == "#") {
            // do nothing
            return
        }

        if (xOffset != 0) {
            // move left or right
            var endOfBoxes = 1
            while (grid[start.y][start.x + xOffset * endOfBoxes].str in "[]") {
                endOfBoxes += 1
            }

            println("moving $endOfBoxes boxes")
            while (endOfBoxes != 0) {
                grid[start.y][start.x + xOffset * endOfBoxes].str = grid[start.y][start.x + xOffset * endOfBoxes - xOffset].str
                endOfBoxes -= 1
            }
            return

        } else {
            // move up or down
            if (checkCoord.str == "[") {
                println("write box left")
                doMoveB(xOffset, yOffset, checkCoord, grid)
                doMoveB(xOffset, yOffset, grid[checkY][checkX + 1], grid)

                checkCoord.str = "."
                grid[checkCoord.y + yOffset][checkCoord.x + xOffset].str = "["
                grid[checkCoord.y][checkCoord.x + 1].str = "."
                grid[checkCoord.y + yOffset][checkCoord.x + 1 + xOffset].str = "]"
                return
            }

            if (checkCoord.str == "]") {
                println("write box right")
                doMoveB(xOffset, yOffset, checkCoord, grid)
                doMoveB(xOffset, yOffset, grid[checkY][checkX - 1], grid)

                checkCoord.str = "."
                grid[checkCoord.y + yOffset][checkCoord.x + xOffset].str = "]"
                grid[checkCoord.y][checkCoord.x - 1].str = "."
                grid[checkCoord.y + yOffset][checkCoord.x - 1 + xOffset].str = "["
                return
            }
        }

        throw IllegalStateException("checkCoord $checkCoord has an unknown value")
    }

    private fun readInstruction(instruction: Char): Pair<Int, Int> {
        return when (instruction) {
            '^' -> 0 to -1
            '>' -> 1 to 0
            'v' -> 0 to 1
            '<' -> -1 to 0
            else -> throw IllegalArgumentException("instruction $instruction not valid instruction")
        }
    }

    data class Coord(val x: Int, val y: Int, var str: String) {
        override fun toString(): String {
            return "[$x][$y]($str)"
        }

        fun toCoordStr(): String {
            return "[$x][$y]"
        }
    }
}