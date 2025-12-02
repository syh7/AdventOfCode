package syh.year2022

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2022, 3) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        return lines.map { findDoubleItemInSameRucksack(it) }
            .sumOf { getPriority(it) }
            .toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        return lines.chunked(3)
            .map { list ->
                val sets = list.map { it.toSet() }
                sets.reduce { a, b -> a.filter { b.contains(it) }.toSet() }
            }
            .sumOf { getPriority(it.first()) }
            .toString()
    }


    private fun findDoubleItemInSameRucksack(line: String): Char {
        val (first, second) = line.chunked(line.length / 2).map { it.toSet() }
        return first.filter { second.contains(it) }[0]
    }

    private fun getPriority(c: Char): Int {
        return if (c.toString() in "A".."Z") {
            c.minus('A') + 1 + 26
        } else {
            c.minus('a') + 1
        }
    }
}
