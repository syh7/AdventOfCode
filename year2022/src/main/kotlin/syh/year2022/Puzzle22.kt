package syh.year2022

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction


class Puzzle22 : AbstractAocDay(2022, 22) {

    override fun doA(file: String): String {
        val blockSize = if (file == "test") 4 else 50
        val faceChanges = giveFaceChangesPartA(file)

        val (gridStr, instructionStr) = readDoubleLineFile(file)

        val gridLines = gridStr.split("\r\n").map {
            it.split("")
                .filter { s -> s.isNotEmpty() }
                .map { c -> BoardOption.entries.first { p -> p.charValue == c } }
        }

        val state = performInstructions(instructionStr, blockSize, faceChanges, file, gridLines)

        val globalCoord = toGlobalCoord(state, blockSize, file)
        val result = calculateValue(globalCoord, state.direction)
        println("final position is $globalCoord with state $state resulting in value $result")
        return result.toString()
    }

    override fun doB(file: String): String {
        val blockSize = if (file == "test") 4 else 50
        val faceChanges = giveFaceChangesPartB(file)

        val (gridStr, instructionStr) = readDoubleLineFile(file)

        val gridLines = gridStr.split("\r\n").map {
            it.split("")
                .filter { s -> s.isNotEmpty() }
                .map { c -> BoardOption.entries.first { p -> p.charValue == c } }
        }

        val state = performInstructions(instructionStr, blockSize, faceChanges, file, gridLines)

        val globalCoord = toGlobalCoord(state, blockSize, file)
        val result = calculateValue(globalCoord, state.direction)
        println("final position is $globalCoord with state $state resulting in value $result")
        return result.toString()
    }

    private fun performInstructions(instructionStr: String, blockSize: Int, faceChanges: Map<String, List<String>>, file: String, gridLines: List<List<BoardOption>>): State {
        var state = State("A", Coord(0, 0), Direction.EAST)

        println("starting with state $state")
        Regex("\\d+|R|L").findAll(instructionStr).forEach { match ->
            when (match.value) {
                "R" -> state = state.copy(direction = state.direction.right90())
                "L" -> state = state.copy(direction = state.direction.left90())
                else -> {
//                    println("checking match ${match.value} with direction ${state.direction}")

                    val steps = match.value.toInt()
                    for (i in 1..steps) {
                        val newState = step(state, blockSize, faceChanges)
                        val globalCoord = toGlobalCoord(newState, blockSize, file)

                        if (gridLines[globalCoord.row][globalCoord.column] == BoardOption.OPEN) {
//                            println("  step $i accepts $globalCoord with state $newState")
                            state = newState
                        } else {
//                            println("  step $i rejects $globalCoord with state $newState")
                            break
                        }
                    }
                    println()
                }
            }
        }
        return state
    }

    private fun calculateValue(position: Coord, direction: Direction): Int {
        val facing = when (direction) {
            Direction.EAST -> 0
            Direction.SOUTH -> 1
            Direction.WEST -> 2
            Direction.NORTH -> 3
            else -> throw IllegalStateException("direction $direction is not NESW")
        }
        return 1000 * (position.row + 1) + 4 * (position.column + 1) + facing
    }

    private fun toGlobalCoord(state: State, blockSize: Int, file: String): Coord {
        return if (file == "test") {
            when (state.currentBlock) {
                "A" -> Coord(row = state.position.row, column = state.position.column + 2 * blockSize)
                "B" -> Coord(row = state.position.row + blockSize, column = state.position.column)
                "C" -> Coord(row = state.position.row + blockSize, column = state.position.column + blockSize)
                "D" -> Coord(row = state.position.row + blockSize, column = state.position.column + 2 * blockSize)
                "E" -> Coord(row = state.position.row + 2 * blockSize, column = state.position.column + 2 * blockSize)
                "F" -> Coord(row = state.position.row + 2 * blockSize, column = state.position.column + 3 * blockSize)
                else -> throw IllegalArgumentException("block ${state.currentBlock} should be between A-F")
            }
        } else {
            when (state.currentBlock) {
                "A" -> Coord(state.position.row, state.position.column + blockSize)
                "B" -> Coord(state.position.row, state.position.column + 2 * blockSize)
                "C" -> Coord(state.position.row + blockSize, state.position.column + blockSize)
                "D" -> Coord(state.position.row + 2 * blockSize, state.position.column)
                "E" -> Coord(state.position.row + 2 * blockSize, state.position.column + blockSize)
                "F" -> Coord(state.position.row + 3 * blockSize, state.position.column)
                else -> throw IllegalArgumentException("block ${state.currentBlock} should be between A-F")
            }
        }
    }

    private fun step(state: State, blockSize: Int, faceChanges: Map<String, List<String>>): State {
        var newPos = state.position.relative(state.direction)
        var newBlock = state.currentBlock
        var newDirection = state.direction

        if (newPos.row !in 0..<blockSize || newPos.column !in 0..<blockSize) {
            val changeIndex = when (state.direction) {
                Direction.EAST -> 0
                Direction.SOUTH -> 1
                Direction.WEST -> 2
                Direction.NORTH -> 3
                else -> throw IllegalStateException("direction ${state.direction} is not NESW")
            }
            val (destination, nrOfRights) = faceChanges[state.currentBlock]!![changeIndex].split("").filter { it.isNotEmpty() }
            newBlock = destination
            println("    changing from block ${state.currentBlock} to $newBlock with $nrOfRights rights")

            // put new position in 0..49 range
            newPos = Coord(
                (newPos.row + blockSize) % blockSize,
                (newPos.column + blockSize) % blockSize,
            )
            // change position to new position in new block, and alter direction
            for (i in 1..nrOfRights.toInt()) {
                newPos = Coord(newPos.column, blockSize - newPos.row - 1)
                newDirection = newDirection.right90()
            }

        }

        return State(newBlock, newPos, newDirection)
    }

    private fun giveFaceChangesPartA(file: String): Map<String, List<String>> {
        // BLOCK -> EAST SOUTH WEST NORTH
        return if (file == "test") {
            mapOf(
                "A" to listOf("A0", "D0", "A0", "E0"),
                "B" to listOf("C0", "B0", "D0", "B0"),
                "C" to listOf("D0", "C0", "B0", "C0"),
                "D" to listOf("B0", "E0", "C0", "A0"),
                "E" to listOf("F0", "A0", "F0", "D0"),
                "F" to listOf("E0", "F0", "E0", "F0"),
            )
        } else {
            mapOf(
                "A" to listOf("B0", "C0", "B0", "E0"),
                "B" to listOf("A0", "B0", "A0", "B0"),
                "C" to listOf("C0", "E0", "C0", "A0"),
                "D" to listOf("E0", "F0", "E0", "F0"),
                "E" to listOf("D0", "A0", "D0", "C0"),
                "F" to listOf("F0", "D0", "F0", "D0"),
            )
        }
    }

    private fun giveFaceChangesPartB(file: String): Map<String, List<String>> {
        // BLOCK -> EAST SOUTH WEST NORTH
        return if (file == "test") {
            //   A
            // BCD
            //   EF
            mapOf(
                "A" to listOf("F2", "D0", "C3", "B2"),
                "B" to listOf("C0", "E2", "F1", "A2"),
                "C" to listOf("D0", "E3", "B0", "A1"),
                "D" to listOf("F1", "E0", "C0", "A0"),
                "E" to listOf("F1", "B2", "C1", "D0"),
                "F" to listOf("A2", "B3", "E0", "D3"),
            )
        } else {
            mapOf(
                "A" to listOf("B0", "C0", "D2", "F1"),
                "B" to listOf("E2", "C1", "A0", "F0"),
                "C" to listOf("B3", "E0", "D3", "A0"),
                "D" to listOf("E0", "F0", "A2", "C1"),
                "E" to listOf("B2", "F1", "D0", "C0"),
                "F" to listOf("E3", "B0", "A3", "D0"),
            )
        }
    }

    enum class BoardOption(val charValue: String) { INVALID(" "), OPEN("."), WALL("#") }
    data class State(val currentBlock: String, val position: Coord, val direction: Direction)
}
