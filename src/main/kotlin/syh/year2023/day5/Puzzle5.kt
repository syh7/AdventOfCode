package syh.year2023.day5

import syh.AbstractAocDay
import kotlin.math.max
import kotlin.math.min

class Puzzle5 : AbstractAocDay(2023, 5) {
    override fun doA(file: String): Long {
        val lines = readSingleLineFile(file)

        val seeds = lines[0]
            .split(": ")[1]
            .split(" ")
            .map { it.toLong() }
        println("seeds: $seeds")

        var lineIndex = 2
        var currentListName = ""
        var currentList = mutableListOf<MapRange>()
        val maps = mutableMapOf<String, MutableList<MapRange>>()

        while (lineIndex < lines.size) {
            val line = lines[lineIndex]
            if (currentListName == "") {
                currentListName = line.split(" ")[0]

            } else if (line == "") {
                maps[currentListName] = currentList
                currentListName = line
                currentList = mutableListOf()

            } else {
                val (destination, source, range) = line.split(" ").map { it.toLong() }
                currentList.add(MapRange(destination, source, source + range))
            }

            lineIndex++
        }
        maps[currentListName] = currentList

        maps.map { it.value }.forEach { println(it) }

        val locationList = seeds.asSequence()
            .map { calculateNewValue(maps["seed-to-soil"]!!, it) }
            .map { calculateNewValue(maps["soil-to-fertilizer"]!!, it) }
            .map { calculateNewValue(maps["fertilizer-to-water"]!!, it) }
            .map { calculateNewValue(maps["water-to-light"]!!, it) }
            .map { calculateNewValue(maps["light-to-temperature"]!!, it) }
            .map { calculateNewValue(maps["temperature-to-humidity"]!!, it) }
            .map { calculateNewValue(maps["humidity-to-location"]!!, it) }
            .toList()

        println(locationList)
        println(locationList.min())
        return locationList.min()
    }

    override fun doB(file: String): Long {
        val lines = readSingleLineFile(file)

        val seedRanges = readSeeds(lines)

        var lineIndex = 2
        var currentListName = ""
        var currentList = mutableListOf<MapRange>()
        val maps = mutableMapOf<String, MutableList<MapRange>>()

        while (lineIndex < lines.size) {
            val line = lines[lineIndex]
            if (currentListName == "") {
                currentListName = line.split(" ")[0]

            } else if (line == "") {
                maps[currentListName] = currentList
                currentListName = line
                currentList = mutableListOf()

            } else {
                val (destination, source, range) = line.split(" ").map { it.toLong() }
                currentList.add(MapRange(destination, source, source + range))
            }

            lineIndex++
        }
        maps[currentListName] = currentList

        maps.map { it.value }.forEach { println(it) }

        return smarterSeedRanges(seedRanges, maps)
    }

    private fun smarterSeedRanges(seedRanges: List<SeedRange>, maps: MutableMap<String, MutableList<MapRange>>): Long {
        val resultRanges = mutableListOf<SeedRange>()
        for (seedRange in seedRanges) {
            var tempResult = mutableListOf(seedRange)
            tempResult = calculateNewValue(maps["seed-to-soil"]!!, tempResult)
            tempResult = calculateNewValue(maps["soil-to-fertilizer"]!!, tempResult)
            tempResult = calculateNewValue(maps["fertilizer-to-water"]!!, tempResult)
            tempResult = calculateNewValue(maps["water-to-light"]!!, tempResult)
            tempResult = calculateNewValue(maps["light-to-temperature"]!!, tempResult)
            tempResult = calculateNewValue(maps["temperature-to-humidity"]!!, tempResult)
            tempResult = calculateNewValue(maps["humidity-to-location"]!!, tempResult)
            resultRanges.addAll(tempResult)
        }

        val lowestLocation = resultRanges.minBy { it.start }

        println(lowestLocation)
        return lowestLocation.start
    }

    private fun readSeeds(lines: List<String>): List<SeedRange> {
        val seedRanges = lines[0].split(": ")[1]
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        println("seedranges: $seedRanges")

        val seeds = mutableListOf<SeedRange>()
        for (i in seedRanges.indices) {
            if (i % 2 == 1) {
                val start = seedRanges[i - 1].toLong()
                val range = seedRanges[i].toLong()
                seeds.add(SeedRange(start, start + range))
            }
        }

        println("seeds: $seeds")
        return seeds
    }

    private fun calculateNewValue(mapRanges: List<MapRange>, value: Long): Long {
        val applicableRange = mapRanges.firstOrNull { it.contains(value) }
        applicableRange?.let { println("applicable range for value $value is range $applicableRange") }
        val newValue = applicableRange?.calculate(value) ?: value
        println("calculated new value from $value to $newValue")
        return newValue
    }

    private fun calculateNewValue(mapRanges: List<MapRange>, initSeeds: List<SeedRange>): MutableList<SeedRange> {
        // instead of bruteforcing and checking each seed in the range
        // only check beginnings and endings of intersecting intervals
        // e.g.
        // [begin1                                       end1) of seed range
        //        [begin2       end2)                          of map range
        // creates more ranges:
        // [begin1, min(end1, begin2)] has not matched an interval, so just continue with this range
        // [max(begin1, begin2), min(end1, end2)] has matched an interval, so update with the offset
        // [max(end2, begin1), end1] has not matched an interval, so just continue with this range
        // it is possible for a range to be empty (or negative), then it can be ignored

        val result = mutableListOf<SeedRange>()
        var leftoverSeedRange = initSeeds.toMutableList()

        println("--------")
        println("calculate new value for init seeds")
        initSeeds.forEach { println(it) }

        for (mapRange in mapRanges) {
            val newSeeds = mutableListOf<SeedRange>()
            for (seedRange in leftoverSeedRange) {
                // create three new ranges if available
                val seedBegin = seedRange.start
                val seedEnd = seedRange.end
                val mapBegin = mapRange.source
                val mapEnd = mapRange.end

                val rangeBefore = SeedRange(start = seedBegin, end = min(seedEnd, mapBegin))
                val rangeInter = SeedRange(start = max(seedBegin, mapBegin), end = min(seedEnd, mapEnd))
                val rangeAfter = SeedRange(start = max(seedEnd, mapBegin), end = seedEnd)

                println("calculated before: $rangeBefore")
                println("calculated inter: $rangeInter")
                println("calculated after: $rangeAfter")

                if (rangeBefore.valid()) newSeeds.add(rangeBefore)
                if (rangeAfter.valid()) newSeeds.add(rangeAfter)

                if (rangeInter.valid()) {
                    val resultSeedRange = SeedRange(start = mapRange.calculate(rangeInter.start), end = mapRange.calculate(rangeInter.end))
                    result.add(resultSeedRange)
                }

                if (!(rangeBefore.valid() || rangeInter.valid() || rangeAfter.valid())) {
                    newSeeds.add(seedRange)
                }
            }
            leftoverSeedRange = newSeeds
            println()
        }
        result.addAll(leftoverSeedRange)
        return result
    }

    data class MapRange(val destination: Long, val source: Long, val end: Long) {
        fun contains(value: Long): Boolean {
            return value in source..<end
        }

        fun calculate(value: Long): Long {
            return value + destination - source
        }
    }

    data class SeedRange(val start: Long, val end: Long) {
        fun contains(value: Long): Boolean {
            return value in start..<end
        }

        fun valid(): Boolean = start < end
    }
}

