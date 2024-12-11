package syh.year2023.day15

import syh.AbstractAocDay

class Puzzle15 : AbstractAocDay(2023, 15) {
    override fun doA(file: String): String {
        val instructions = readSingleLineFile(file)[0].split(",")

        val totalSum = instructions.sumOf { calculateHash(it) }
        println("total hash sum is $totalSum")

        return totalSum.toString()
    }

    override fun doB(file: String): String {
        val instructions = readSingleLineFile(file)[0].split(",")

        val boxes = HashMap<Int, LinkedHashMap<String, Int>>(256)
        for (i in 0..255) {
            boxes[i] = linkedMapOf()
        }

        for (instruction in instructions) {

            if (instruction.contains("=")) {
                val (str, value) = instruction.split("=")
                val boxNr = calculateHash(str)
                boxes[boxNr]!![str] = value.toInt()

            } else if (instruction.contains("-")) {
                val str = instruction.removeSuffix("-")
                val boxNr = calculateHash(str)
                boxes[boxNr]!!.remove(str)
            }
        }

        val totalFocus = boxes.entries.sumOf { (boxNr, lensesMap) ->
            if (lensesMap.isEmpty()) {
                0
            } else {
                var totalBoxFocus = 0
                lensesMap.onEachIndexed { i: Int, entry: Map.Entry<String, Int> ->
                    val lensFocus = (boxNr + 1) * (i + 1) * entry.value
                    totalBoxFocus += lensFocus
                }
                totalBoxFocus
            }
        }
        println("total focus: $totalFocus")
        return totalFocus.toString()
    }

    private fun calculateHash(instruction: String): Int {
        var hash = 0
        for (c in instruction) {
            hash += c.code
            hash *= 17
            hash %= 256
        }
        return hash
    }
}