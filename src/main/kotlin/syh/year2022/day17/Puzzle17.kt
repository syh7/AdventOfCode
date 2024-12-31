package syh.year2022.day17

import syh.AbstractAocDay
import syh.library.Coord
import syh.library.Direction


class Puzzle17 : AbstractAocDay(2022, 17) {

    override fun doA(file: String): String {
        val instructions = readSingleLineFile(file)[0].split("").filter { it.isNotEmpty() }

        val height = calculateHeight(instructions, 2022)
        println("height: $height")
        return height.toString()
    }

    override fun doB(file: String): String {
        val instructions = readSingleLineFile(file)[0].split("").filter { it.isNotEmpty() }

        val height = calculateHeight(instructions, 1000000000000)
        println("height: $height")
        return height.toString()
    }

    private fun calculateHeight(instructions: List<String>, limit: Long): Long {
        var rockCounter = 0L
        var height = 0
        var cycleHeight = 0L
        var instructionIndex = 0

        val mountain = (0..7).map { Coord(0, it) }.toMutableSet()

        val rockInstructionCache = mutableMapOf<Triple<Int, Int, Coord>, Pair<Long, Int>>()

        while (rockCounter < limit) {
            val rockType = (rockCounter % 5).toInt()

            var rock = getRock(rockType, height + 4)
            var totalDifference = Coord(0, 0)

            while (true) {
                val direction = getDirection(instructions[instructionIndex])
                instructionIndex = (instructionIndex + 1) % instructions.size
                val movedRock = move(rock, direction)
                if (validPosition(movedRock, mountain)) {
//                    println("valid move $instruction resulting in rock $movedRock")
                    rock = movedRock
                    totalDifference = totalDifference.relative(direction)
                }

                val fallenRock = move(rock, Direction(-1, 0))
                if (validPosition(fallenRock, mountain)) {
//                    println("valid fall")
                    rock = fallenRock
                    totalDifference = totalDifference.relative(Direction(-1, 0))
                } else {
//                    println("end of , rock $rock")
                    mountain.addAll(rock)
                    height = mountain.maxOf { it.row }


                    if (rockInstructionCache.containsKey(Triple(rockType, instructionIndex, totalDifference))) {
                        // found cycle
                        // add remaining cycles in one swoop to height
                        val (oldCounter, oldHeight) = rockInstructionCache[Triple(rockType, instructionIndex, totalDifference)]!!
                        val heightDiff = height - oldHeight
                        val counterDiff = rockCounter - oldCounter
                        val cyclesRemaining = (limit - rockCounter) / counterDiff
                        if (cyclesRemaining != 0L) {
                            println("found cycle at $rockCounter with height $height, with rock $rockType and instruction $instructionIndex")
                            println("start of cycle was at $oldCounter with height $oldHeight")
                            println("cycle took $counterDiff rocks, changed height for $heightDiff")
                            println("calculated cycles remaining is $cyclesRemaining, skipping ${cyclesRemaining * counterDiff} and adding height ${cyclesRemaining * heightDiff}")
                            println()
                        }
                        cycleHeight += cyclesRemaining * heightDiff
                        rockCounter += cyclesRemaining * counterDiff
                    }

                    if (rockCounter > 500) {
                        // give it time to settle into a rhythm
                        rockInstructionCache[Triple(rockType, instructionIndex, totalDifference)] = rockCounter to height
                    }
                    break
                }
            }

//            println("rock $rockCounter with type $rockType ended at instruction $instructionIndex")
//            printMountain(mountain)
//            println()

            rockCounter += 1

        }
        return height + cycleHeight
    }

    private fun printMountain(mountain: Set<Coord>) {
        val height = mountain.maxOf { it.row }
        for (row in height downTo 0) {
            var str = ""
            for (col in 0..6) {
                str += if (Coord(row, col) in mountain) "#" else "."
            }
            println(str)
        }
    }

    private fun validPosition(rock: List<Coord>, mountain: Set<Coord>): Boolean {
        return rock.all { it.column in 0..<7 } && rock.none { it in mountain }
    }

    private fun move(coords: List<Coord>, direction: Direction): List<Coord> {
        return coords.map { it.relative(direction) }
    }

    private fun getDirection(dirStr: String): Direction {
        val direction = when (dirStr) {
            ">" -> Direction(0, 1)
            "<" -> Direction(0, -1)
            else -> throw IllegalStateException("unknown direction $dirStr")
        }
        return direction
    }

    private fun getRock(rockType: Int, height: Int): List<Coord> {
        return when (rockType) {
            0 -> listOf(Coord(height, 2), Coord(height, 3), Coord(height, 4), Coord(height, 5))
            1 -> listOf(Coord(height + 2, 3), Coord(height + 1, 2), Coord(height + 1, 3), Coord(height + 1, 4), Coord(height, 3))
            2 -> listOf(Coord(height + 2, 4), Coord(height + 1, 4), Coord(height, 4), Coord(height, 3), Coord(height, 2))
            3 -> listOf(Coord(height + 3, 2), Coord(height + 2, 2), Coord(height + 1, 2), Coord(height, 2))
            4 -> listOf(Coord(height + 1, 2), Coord(height + 1, 3), Coord(height, 2), Coord(height, 3))
            else -> throw IllegalStateException("rock type $rockType should not be over 4")
        }
    }
}
