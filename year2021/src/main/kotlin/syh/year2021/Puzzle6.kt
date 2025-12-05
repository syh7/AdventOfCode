package syh.year2021

import syh.library.AbstractAocDay

class Puzzle6 : AbstractAocDay(2021, 6) {
    override fun doA(file: String): String {
        return doPuzzle(file, 80)
    }

    override fun doB(file: String): String {
        return doPuzzle(file, 256)
    }

    private fun doPuzzle(file: String, nrOfRounds: Long): String {
        val startPhase = readSingleLineFile(file)[0].split(",").map { it.toLong() }

        var map = mutableMapOf<Long, Long>()
        for (fish in startPhase) {
            map.putIfAbsent(fish, 0)
            map.computeIfPresent(fish) { _, prev -> prev + 1 }
        }

        println(map)

        for (i in 1..nrOfRounds) {
            val tempMap = mutableMapOf<Long, Long>()
            for ((fish, count) in map) {
                if (fish == 0L) {
                    tempMap[6] = (tempMap[6] ?: 0) + count
                    tempMap[8] = (tempMap[8] ?: 0) + count
                } else {
                    tempMap[fish - 1] = (tempMap[fish - 1] ?: 0) + count
                }
            }
            map = tempMap
            println(map)
        }

        return map.values.sum().toString()
    }

}