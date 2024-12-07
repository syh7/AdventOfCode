package syh.year2024.day5

import syh.AbstractAocDay

class Puzzle5 : AbstractAocDay(2024, 5) {
    override fun doA(file: String): Long {
        val (orderingLines, manualLines) = readDoubleLineFile(file)
        val ordering = orderingLines.split("\r\n")
            .map {
                val (before, after) = it.split("|")
                before.toInt() to after.toInt()
            }
        val manuals = manualLines.split("\r\n")
            .map { line -> line.split(",").map { it.toInt() } }

        val correctManuals = manuals.filter { isCorrect(it, ordering) }
        println("correct manuals = $correctManuals")

        val totalCorrect = correctManuals.sumOf { it[it.size / 2] }
        println("total for A: $totalCorrect")

        return totalCorrect.toLong()
    }

    override fun doB(file: String): Long {
        val (orderingLines, manualLines) = readDoubleLineFile(file)
        val ordering = orderingLines.split("\r\n")
            .map {
                val (before, after) = it.split("|")
                before.toInt() to after.toInt()
            }
        val manuals = manualLines.split("\r\n")
            .map { line -> line.split(",").map { it.toInt() } }

        val incorrectManuals = manuals.filterNot { isCorrect(it, ordering) }
        println("incorrect manuals = $incorrectManuals")

        val totalIncorrect = incorrectManuals
            .map { improveManual(it, ordering) }
            .sumOf { it[it.size / 2] }
        println("total for B: $totalIncorrect")
        return totalIncorrect.toLong()
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
        val comparator: Comparator<Int> = Comparator { a, b ->
            if (ordering.firstOrNull { it.first == a && it.second == b } != null) {
                1
            } else {
                -1
            }
        }
        return manual.toSortedSet(comparator).toList()
    }
}