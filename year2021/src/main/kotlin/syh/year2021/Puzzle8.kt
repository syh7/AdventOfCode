package syh.year2021

import syh.library.AbstractAocDay

class Puzzle8 : AbstractAocDay(2021, 8) {
    override fun doA(file: String): String {
        val displays = readSingleLineFile(file).map { it.split(" | ") }
        return displays.map { it[1] }
            .also { println(it) }
            .map { it.split(" ") }
            .map { it.filter { str -> str.length in listOf(2, 4, 3, 7) } }
            .also { println(it) }
            .sumOf { it.size }
            .toString()

    }

    override fun doB(file: String): String {
        return readSingleLineFile(file)
            .asSequence()
            .map { it.split(" | ") }
            .map { it[0].split(" ") to it[1].split(" ") }
            .map { (inputDisplay, outputDisplay) -> inputDisplay.map { it.sorted() } to outputDisplay.map { it.sorted() } }
            .map { (inputDisplay, outputDisplay) -> calculateWireSegmentMapping(inputDisplay) to outputDisplay }
            .map { (map, outputDisplay) -> calculateNumber(outputDisplay, map) }
            .sum()
            .toString()
    }

    private fun calculateWireSegmentMapping(inputDisplay: List<String>): Map<String, Int> {
        val nr1 = inputDisplay.first { it.length == 2 }
        val nr4 = inputDisplay.first { it.length == 4 }
        val nr7 = inputDisplay.first { it.length == 3 }
        val nr8 = inputDisplay.first { it.length == 7 }
        val length5 = inputDisplay.filter { it.length == 5 }
        val length6 = inputDisplay.filter { it.length == 6 }

        //   0:      1:      2:      3:      4:
        //  aaaa    ....    aaaa    aaaa    ....
        // b    c  .    c  .    c  .    c  b    c
        // b    c  .    c  .    c  .    c  b    c
        //  ....    ....    dddd    dddd    dddd
        // e    f  .    f  e    .  .    f  .    f
        // e    f  .    f  e    .  .    f  .    f
        //  gggg    ....    gggg    gggg    ....
        //
        //   5:      6:      7:      8:      9:
        //  aaaa    aaaa    aaaa    aaaa    aaaa
        // b    .  b    .  .    c  b    c  b    c
        // b    .  b    .  .    c  b    c  b    c
        //  dddd    dddd    ....    dddd    dddd
        // .    f  e    f  .    f  e    f  .    f
        // .    f  e    f  .    f  e    f  .    f
        //  gggg    gggg    ....    gggg    gggg

        // length 2 = 1
        // length 3 = 7
        // length 4 = 4
        // length 5 = 2,3,5
        // length 6 = 0,6,9
        // length 7 = 8
        // 4 - 1 gives bd
        // bd and length 5 gives  5
        // NOT bd and length 6 gives 0
        // 0 - 7 - 4 gives eg
        // eg and length 5 gives 2
        // not 2,3 and length 5 gives 3
        // eg and length 6 and not 0 gives 6
        // not 0,6 and length 6 gives 9

        val bd = nr4.minus(nr1)
        val nr5 = length5.first { it.contains(bd[0]) && it.contains(bd[1]) }
        val nr0 = length6.first { !it.contains(bd[0]) || !it.contains(bd[1]) }

        val eg = nr0.minus(nr7).minus(nr4)

        val nr2 = length5.first { it.contains(eg[0]) && it.contains(eg[1]) }
        val nr3 = length5.first { it != nr2 && it != nr5 }

        val nr6 = length6.filter { it != nr0 }.first { it.contains(eg[0]) && it.contains(eg[1]) }
        val nr9 = length6.first { it != nr0 && it != nr6 }

        val map = mapOf(
            nr0 to 0,
            nr1 to 1,
            nr2 to 2,
            nr3 to 3,
            nr4 to 4,
            nr5 to 5,
            nr6 to 6,
            nr7 to 7,
            nr8 to 8,
            nr9 to 9,
        )
        println("Calculated $inputDisplay to mean $map")
        return map
    }

    private fun calculateNumber(outputDisplay: List<String>, wireSegmentMap: Map<String, Int>): Int {
        var totalStr = ""
        for (wires in outputDisplay) {
            totalStr += wireSegmentMap[wires]
        }
        println("Total $totalStr for $outputDisplay")
        return totalStr.toInt()
    }

    private fun String.sorted() = this.toCharArray().sorted().joinToString("")
    private fun String.minus(other: String) = this.filter { !other.contains(it) }

}