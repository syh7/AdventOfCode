package syh.year2024.day5

import syh.readDoubleLineFile

fun main() {

    val (orderingLines, manualLines) = readDoubleLineFile("year2024/day5/actual.txt")
    val ordering = orderingLines.split("\r\n")
        .map {
            val (before, after) = it.split("|")
            before.toInt() to after.toInt()
        }
    val manuals = manualLines.split("\r\n")
        .map { line -> line.split(",").map { it.toInt() } }

    val groupedManuals = manuals.groupBy { isCorrect(it, ordering) }
    val correctManuals = groupedManuals[true]!!
    println("correct manuals = $correctManuals")

    val totalCorrect = correctManuals.sumOf { it[it.size / 2] }
    println("total for A: $totalCorrect")

    val incorrectManuals = groupedManuals[false]!!
    println("incorrect manuals = $incorrectManuals")

    val totalIncorrect = incorrectManuals
        .map { improveManual(it, ordering) }
        .sumOf { it[it.size / 2] }
    println("total for B: $totalIncorrect")


}

private fun isCorrect(manual: List<Int>, ordering: List<Pair<Int, Int>>): Boolean {
    for (i in manual.indices) {
        for (j in i + 1 until manual.size) {
            ordering.firstOrNull { it.first == manual[j] && it.second == manual[i] }
                ?.let {
                    println("incorrect order on $i=${manual[i]}, $j=${manual[j]}")
                    println(manual)
                    return false
                }
        }
    }
    return true
}

private fun improveManual(manual: List<Int>, ordering: List<Pair<Int, Int>>): List<Int> {
    val comparator: Comparator<Int> = object : Comparator<Int> {
        override fun compare(a: Int, b: Int): Int {
            if (ordering.firstOrNull { it.first == a && it.second == b } != null) {
                return 1
            } else {
                return -1
            }
        }
    }
    return manual.toSortedSet(comparator).toList()
}