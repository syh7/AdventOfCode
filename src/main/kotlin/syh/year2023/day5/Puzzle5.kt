package syh.year2023.day5

import syh.AbstractAocDay

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
                currentList.add(MapRange(destination, source, range))
            }

            lineIndex++
        }
        maps[currentListName] = currentList

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
                currentList.add(MapRange(destination, source, range))
            }

            lineIndex++
        }
        maps[currentListName] = currentList

        var lowestLocation: Long? = null
        seedRanges.forEach { seedRange ->
            println("Checking seed range $seedRange")
            for (seed in seedRange.start..<seedRange.start + seedRange.range) {
                if (seed % 1000000L == 0L) {
                    println("still working! $seed")
                }
                var tempLocation = seed
                tempLocation = calculateNewValue(maps["seed-to-soil"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["soil-to-fertilizer"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["fertilizer-to-water"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["water-to-light"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["light-to-temperature"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["temperature-to-humidity"]!!, tempLocation)
                tempLocation = calculateNewValue(maps["humidity-to-location"]!!, tempLocation)
                if (lowestLocation == null || tempLocation < lowestLocation!!) {
                    println("found lower location $tempLocation")
                    lowestLocation = tempLocation
                }
            }
        }

        println(lowestLocation)
        return lowestLocation!!.toLong()
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
                seeds.add(SeedRange(seedRanges[i - 1].toLong(), seedRanges[i].toLong()))
            }
        }

        println("seeds: $seeds")
        return seeds
    }

    private fun calculateNewValue(mapRanges: List<MapRange>, value: Long): Long {
        return mapRanges.firstOrNull { it.contains(value) }?.calculate(value) ?: value
    }

    data class MapRange(val destination: Long, val source: Long, val range: Long) {
        fun contains(value: Long): Boolean {
            return value in source..<source + range
        }

        fun calculate(value: Long): Long {
            return value + destination - source
        }
    }

    data class SeedRange(val start: Long, val range: Long) {
        fun contains(value: Long): Boolean {
            return value in start..<start + range
        }
    }
}

