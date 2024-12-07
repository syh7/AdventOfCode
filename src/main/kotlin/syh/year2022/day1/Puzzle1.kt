package syh.year2022.day1

import syh.readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2022/day1/actual.txt")

    val elves = mutableListOf<Int>()
    var tempElf = 0
    for (line in lines) {
        println(line)
        if (line.toIntOrNull() != null) {
            tempElf += line.toInt()
            println("added $line to temp elf for max $tempElf")
        } else {
            elves.add(tempElf)
            tempElf = 0
        }
    }
    if (tempElf != 0) {
        elves.add(tempElf)
    }

    val max1 = elves.max()
    elves.remove(max1)
    val max2 = elves.max()
    elves.remove(max2)
    val max3 = elves.max()
    elves.remove(max3)



    println("max1: $max1")
    println("max total: ${max1 + max2 + max3}")
}