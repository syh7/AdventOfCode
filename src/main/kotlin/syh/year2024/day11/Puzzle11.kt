package syh.year2024.day11

import syh.AbstractAocDay


class Puzzle11 : AbstractAocDay(2024, 11) {
    override fun doA(file: String): String {
        val stones = readSingleLineFile(file)[0]
            .split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

        return calculateNewStones(stones, 25).toString()
    }

    override fun doB(file: String): String {
        val stones = readSingleLineFile(file)[0]
            .split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

        return calculateNewStones(stones, 75).toString()
    }

    private fun calculateNewStones(stones: List<Long>, iterationsLeft: Int): Long {
        var map = mutableMapOf<Long, Long>()
        for (stone in stones) {
            map.computeIfAbsent(stone) { 1L }
        }

        for (i in 1..iterationsLeft) {
            val newMap = mutableMapOf<Long, Long>()

            for ((currentStone, currentValue) in map) {
                val newStones = calculateNewStones(currentStone)
                for (newStone in newStones) {
                    newMap[newStone] = newMap.getOrDefault(newStone, 0L) + currentValue
                }
            }

            map = newMap
        }
        return map.values.sum()
    }

    private fun calculateNewStones(stone: Long): List<Long> {
        return when {
            stone == 0L -> listOf(1)
            stone.toString().length % 2 == 0 -> {
                val str = stone.toString()
                val left = str.substring(0, str.length / 2)
                val right = str.substring(str.length / 2)
                listOf(left.toLong(), right.toLong())
            }

            else -> listOf(stone * 2024)
        }
    }
}