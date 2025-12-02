package syh.year2023

import syh.library.AbstractAocDay

class Puzzle2 : AbstractAocDay(2023, 2) {
    override fun doA(file: String): String {
        val total = readSingleLineFile(file)
            .map { readGame(it) }
            .filter { gameIsPossible(it) }
            .sumOf { it.id }
        println("total: $total")
        return total.toString()
    }

    override fun doB(file: String): String {
        val total = readSingleLineFile(file)
            .map { readGame(it) }
            .map { getMinimumSet(it) }
            .sumOf { it.reds * it.greens * it.blues }
        println("total: $total")
        return total.toString()
    }

    private fun gameIsPossible(game: Game): Boolean {
        return game.sets.none { it.reds > 12 || it.blues > 14 || it.greens > 13 }
    }

    private fun getMinimumSet(game: Game): CubeSet {
        val minimumReds = game.sets.maxOf { it.reds }
        val minimumGreens = game.sets.maxOf { it.greens }
        val minimumBlues = game.sets.maxOf { it.blues }
        return CubeSet(reds = minimumReds, greens = minimumGreens, blues = minimumBlues)
    }

    data class Game(
        val id: Int,
        val sets: List<CubeSet>
    )

    data class CubeSet(
        var reds: Int = 0,
        var greens: Int = 0,
        var blues: Int = 0,
    )

    private fun readGame(str: String): Game {
        val (gameId, unconvertedSets) = str.split(": ")

        val id = gameId.split(" ")[1].toInt()
        val sets = unconvertedSets.split("; ")
            .map { set ->
                val cubeSet = CubeSet()
                set.split(", ").forEach { str ->
                    val (amount, colour) = str.split(" ")
                    if (colour == "red") {
                        cubeSet.reds = amount.toInt()
                    }
                    if (colour == "blue") {
                        cubeSet.blues = amount.toInt()
                    }
                    if (colour == "green") {
                        cubeSet.greens = amount.toInt()
                    }
                }
                cubeSet
            }
        return Game(id, sets)
    }

}
