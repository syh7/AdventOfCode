package syh.year2023.day2

import syh.readSingleLineFile


fun main() {
    val total = readSingleLineFile("year2023/day2/actual.txt")
        .map { readGame(it) }
        .map { getMinimumSet(it) }
        .sumOf { it.reds * it.greens * it.blues }
    println("total: $total")
}

private fun getMinimumSet(game: Game): CubeSet {
    val minimumReds = game.sets.maxOf { it.reds }
    val minimumGreens = game.sets.maxOf { it.greens }
    val minimumBlues = game.sets.maxOf { it.blues }
    return CubeSet(reds = minimumReds, greens = minimumGreens, blues = minimumBlues)
}
