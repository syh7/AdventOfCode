package syh.year2021

import syh.library.AbstractAocDay

class Puzzle2 : AbstractAocDay(2021, 2) {
    override fun doA(file: String): String {
        var horizontal = 0
        var depth = 0

        readSingleLineFile(file).forEach { line ->
            val (instr, value) = line.split(" ")
            when (instr) {
                "forward" -> horizontal += value.toInt()
                "down" -> depth -= value.toInt()
                "up" -> depth += value.toInt()
            }
        }

        return (horizontal * -depth).toString()
    }

    override fun doB(file: String): String {
        var horizontal = 0
        var depth = 0
        var aim = 0

        readSingleLineFile(file).forEach { line ->
            val (instr, value) = line.split(" ")
            when (instr) {
                "forward" -> {
                    horizontal += value.toInt()
                    depth += aim * value.toInt()
                }

                "down" -> {
                    aim += value.toInt()
                }

                "up" -> {
                    aim -= value.toInt()
                }
            }
        }

        return (horizontal * depth).toString()
    }
}