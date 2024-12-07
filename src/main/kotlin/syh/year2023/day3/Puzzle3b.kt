package syh.year2023.day3

import syh.readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2023/day3/actual.txt")
        .map { ".$it." } // add . at beginning and end for simpler border checking
        .toMutableList()

    val lineLength = lines[0].length
    lines.add(".".repeat(lineLength))
    lines.add(0, ".".repeat(lineLength))
    println("padded lines")
    lines.forEach { println(it) }
    println()


    var total = 0;


    var gearLineIndex = 1
    while (gearLineIndex < lines.size) {

        var gearColumnIndex = 1
        while (gearColumnIndex < lineLength) {
            if (isGearToken(lines, gearLineIndex, gearColumnIndex)) {
                println("Found gear at [$gearLineIndex,$gearColumnIndex]")
                val numbers = mutableListOf<Int>()

                for (numberLineIndex in gearLineIndex - 1..gearLineIndex + 1) {
                    var numberColumnIndex = gearColumnIndex - 1
                    while (numberColumnIndex <= gearColumnIndex + 1) {
                        if (lines[numberLineIndex][numberColumnIndex].isDigit()) {
                            println("Found number at [$numberLineIndex,$numberColumnIndex]")
                            var numStart = numberColumnIndex
                            var numEnd = numberColumnIndex
                            while (lines[numberLineIndex][numStart].isDigit()) {
                                numStart--
                            }
                            numStart += 1
                            while (lines[numberLineIndex][numEnd].isDigit()) {
                                numEnd++
                            }
                            numEnd -= 1
                            numberColumnIndex = numEnd

                            val slice = lines[numberLineIndex].slice(numStart..numEnd)
                            println("Total number for slice [$numberLineIndex][$numStart, $numEnd] is $slice")
                            numbers.add(slice.toInt())
                        }
                        numberColumnIndex++
                    }
                }
                println("numbers found: $numbers")
                if (numbers.size == 2) {
                    val multiplied = numbers.reduce { a, b -> a * b }
                    total += multiplied
                } else {
                    println("Check here!!!")
                    println()
                }

            }


            gearColumnIndex++
        }

        println()
        gearLineIndex++
    }

    println("total: $total")
}

private fun isGearToken(lines: List<String>, i: Int, j: Int): Boolean {
    return lines[i][j] == '*'
}
