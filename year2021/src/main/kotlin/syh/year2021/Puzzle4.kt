package syh.year2021

import syh.library.AbstractAocDay


class Puzzle4 : AbstractAocDay(2021, 4) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val numbers = lines[0].split(",").map { it.toInt() }
        val boards = lines.drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { chunk ->
                chunk.map { row -> row.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
            }

        for (i in numbers.indices) {
            val drawn = numbers.take(i)
            boards.firstOrNull { isWin(it, drawn) }
                ?.let { board ->
                    val leftoverSum = board.flatten().filter { it !in drawn }.sum()
                    val lastNumber = drawn.last()
                    println("found winning board with leftover $leftoverSum and last number $lastNumber")
                    return (leftoverSum * lastNumber).toString()
                }
        }

        return "could not find winning board"
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val numbers = lines[0].split(",").map { it.toInt() }
        val boards = lines.drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { chunk ->
                chunk.map { row -> row.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
            }

        for (i in numbers.indices.reversed()) {
            val drawn = numbers.take(i)
            boards.firstOrNull { !isWin(it, drawn) }
                ?.let { board ->
                    // found the place before the last win
                    // now up index by one so we have the last win instead of the place before
                    val lastWinDrawn = numbers.take(i + 1)
                    val leftoverSum = board.flatten().filter { it !in lastWinDrawn }.sum()
                    val lastNumber = lastWinDrawn.last()
                    println("found losing board with leftover $leftoverSum and last number $lastNumber")
                    return (leftoverSum * lastNumber).toString()
                }
        }

        return "could not find losing board"
    }

    private fun isWin(board: List<List<Int>>, drawn: List<Int>): Boolean {
        return board.any { row -> row.all { n -> n in drawn } }
                || (0..4).any { col -> board.all { row -> row[col] in drawn } }
    }
}