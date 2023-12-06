package year2023.day3

import readFile

fun main() {
    val lines = readFile("year2023/day3/actual.txt")
        .map { ".$it." } // add . at beginning and end for simpler border checking
        .toMutableList()

    val lineLength = lines[0].length
    lines.add(".".repeat(lineLength))
    lines.add(0, ".".repeat(lineLength))
    println("padded lines")
    println()


    var total = 0;


    var lineIndex = 1
    while (lineIndex < lines.size) {

        var columnIndex = 1
        while (columnIndex < lineLength) {
            if (lines[lineIndex][columnIndex].isDigit()) {
                println("Found number at [$lineIndex,$columnIndex]")
                var number = ""
                val startIndex = columnIndex
                while (columnIndex < lineLength && lines[lineIndex][columnIndex].isDigit()) {
                    number += lines[lineIndex][columnIndex]
                    columnIndex++
                }
                val endIndex = columnIndex - 1
                println("Found number $number from indexes $startIndex - $endIndex")

                for (specialCharLineIndex in lineIndex - 1..lineIndex + 1) {
                    for (specialCharColumnIndex in startIndex - 1..endIndex + 1) {
                        if (isSpecialToken(lines, specialCharLineIndex, specialCharColumnIndex)) {
                            println("Found special token on [$specialCharLineIndex, $specialCharColumnIndex] so adding number $number")
                            total += number.toInt()
                        }
                    }
                }
            }

            columnIndex++
        }
        lineIndex++
    }
    println("total: $total")
}

private fun isSpecialToken(lines: List<String>, i: Int, j: Int): Boolean {
    return !lines[i][j].isDigit() && lines[i][j] != '.'
}
