package syh.year2023.day2

import syh.readSingleLineFile


fun main() {
    val total = readSingleLineFile("year2023/day2/actual.txt")
        .map { readGame(it) }
        .filter { gameIsPossible(it) }
        .sumOf { it.id }
    println("total: $total")
}

private fun gameIsPossible(game: Game): Boolean {
    return game.sets.none { it.reds > 12 || it.blues > 14 || it.greens > 13 }
}
