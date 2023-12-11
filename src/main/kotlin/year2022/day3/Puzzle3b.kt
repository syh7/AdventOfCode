package year2022.day3

import readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2022/day3/actual.txt")

    var firstElf = ""
    var secondElf = ""
    var thirdElf = ""

    var total = 0

    for (i in lines.indices) {
        if (firstElf == "") {
            firstElf = lines[i]
            continue
        } else if (secondElf == "") {
            secondElf = lines[i]
            continue
        } else {
            thirdElf = lines[i]

            val duplicates = firstElf.toSet()
                .filter { secondElf.toSet().contains(it) }
                .filter { thirdElf.toSet().contains(it) }
            println()
            println(firstElf)
            println(secondElf)
            println(thirdElf)
            println(duplicates)
            println()

            total += getPriority(duplicates[0])

            firstElf = ""
            secondElf = ""
            thirdElf = ""

        }
    }

    println("total: $total")
}

private fun getPriority(c: Char): Int {
    return if (c.toString() in "A".."Z") {
        c.minus('A') + 1 + 26
    } else {
        c.minus('a') + 1
    }
}
