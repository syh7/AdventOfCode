package syh.year2023.day5

import syh.readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2023/day5/actual.txt")

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
        println("Checkign seed range $seedRange")
        for (seed in seedRange.start until seedRange.start + seedRange.range) {
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