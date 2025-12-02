package syh.year2021

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2021, 3) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { itt -> itt.isNotEmpty() } }

        val length = lines[0].size - 1
        val totalLines = lines.size
        val oneCounts = (0..length).associateWith { index -> lines.count { it[index] == "1" } }

        var gamma = ""
        var epsilon = ""

        for (i in 0..length) {
            val ones = oneCounts[i]!!
            val zeros = totalLines - ones
//            println("index $i has $ones ones and thus $zeros zeros")
            gamma += if (ones > zeros) 1 else 0
            epsilon += if (ones < zeros) 1 else 0
        }


        val gammaInt = gamma.toInt(2)
        val epsilonInt = epsilon.toInt(2)

        println("gamma: $gamma becomes $gammaInt")
        println("epsilon: $epsilon becomes $epsilonInt")

        return (gammaInt * epsilonInt).toString()

    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val length = lines[0].length - 1
        val totalLines = lines.size

        var oxygenLines = lines
        for (i in 0..length) {
            oxygenLines = filterStrings(oxygenLines, i, true)
        }

        var co2ScrubberLines = lines
        for (i in 0..length) {
            co2ScrubberLines = filterStrings(co2ScrubberLines, i, false)
        }

        val oxygenInt = oxygenLines[0].toInt(2)
        val co2Int = co2ScrubberLines[0].toInt(2)

        println("oxygenInt: $oxygenLines becomes $oxygenInt")
        println("co2Int: $co2ScrubberLines becomes $co2Int")

        return (oxygenInt * co2Int).toString()
    }

    private fun filterStrings(strings: List<String>, index: Int, keepMax: Boolean): List<String> {
        if (strings.size == 1) return strings
        val (ones, zeros) = strings.partition { it[index] == '1' }
        return if (keepMax) {
            if (ones.size >= zeros.size) {
                ones
            } else {
                zeros
            }
        } else {
            if (zeros.size <= ones.size) {
                zeros
            } else {
                ones
            }
        }
    }

}