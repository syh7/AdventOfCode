package syh.year2023

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2023, 3) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
            .map { ".$it." } // add . at beginning and end for simpler border checking
            .toMutableList()

        val lineLength = lines[0].length
        lines.add(".".repeat(lineLength))
        lines.add(0, ".".repeat(lineLength))
        println("padded lines")
        println()

        var total = 0

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
        return total.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
            .map { ".$it." } // add . at beginning and end for simpler border checking
            .toMutableList()

        val lineLength = lines[0].length
        lines.add(".".repeat(lineLength))
        lines.add(0, ".".repeat(lineLength))
        println("padded lines")
        lines.forEach { println(it) }
        println()

        var total = 0

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
        return total.toString()
    }


    private fun isSpecialToken(lines: List<String>, i: Int, j: Int): Boolean {
        return !lines[i][j].isDigit() && lines[i][j] != '.'
    }

    private fun isGearToken(lines: List<String>, i: Int, j: Int): Boolean {
        return lines[i][j] == '*'
    }
}