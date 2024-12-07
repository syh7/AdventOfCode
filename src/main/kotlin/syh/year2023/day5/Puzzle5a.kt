package syh.year2023.day5

import syh.readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2023/day5/actual.txt")

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
}

private fun calculateNewValue(mapRanges: List<MapRange>, value: Long): Long {
    return mapRanges.firstOrNull { it.contains(value) }?.calculate(value) ?: value
}

private fun bufferMadness(seeds: List<Long>, lines: List<String>): MutableList<Long> {
    var i = 2

    var buffer = mutableListOf<Long>()
    var found = mutableListOf<Long>()
    var currentValues = seeds.toMutableList()

    while (i < lines.size) {
        if (lines[i] == "") {
            println("found: $found")
            found.forEach { currentValues.remove(it) }
            println("copying not found: $currentValues")
            currentValues.forEach { buffer.add(it) }

            currentValues = buffer
            buffer = mutableListOf()
            found = mutableListOf()

        } else if (lines[i].contains(":")) {
            println("moving to ${lines[i].split(" ")}")

        } else {
            val (destination, source, range) = lines[i].split(" ").map { it.toLong() }
            currentValues.filter { it in source..source + range }
                .map {
                    println("found value $it in $destination $source $range")
                    found.add(it)
                    it + destination - source
                }
                .forEach {
                    println("adding $it to buffer")
                    buffer.add(it)
                }
        }

        i++
    }

    println(currentValues)
    return currentValues
}

private fun ranOutOfHeapMemory(lines: List<String>, seeds: List<Long>): List<Long> {
    var lineIndex = 2
    var currentMapName = ""
    var currentMap = HashMap<Long, Long>().toMutableMap()
    val maps = HashMap<String, MutableMap<Long, Long>>().toMutableMap()

    while (lineIndex < lines.size) {
        val line = lines[lineIndex]
        if (currentMapName == "") {
            currentMapName = line.split(" ")[0]

        } else if (line == "") {
            maps[currentMapName] = currentMap
            currentMapName = line
            currentMap = HashMap<Long, Long>().toMutableMap()

        } else {
            val (destination, source, range) = line.split(" ").map { it.toLong() }
            for (j in 0 until range) {
                currentMap[source + j] = destination + j
            }
        }

        lineIndex++
    }
    maps[currentMapName] = currentMap

    maps.forEach { println(it) }

    val locationList = seeds.asSequence()
        .map { maps["seed-to-soil"]!![it] ?: it }
        .map { maps["soil-to-fertilizer"]!![it] ?: it }
        .map { maps["fertilizer-to-water"]!![it] ?: it }
        .map { maps["water-to-light"]!![it] ?: it }
        .map { maps["light-to-temperature"]!![it] ?: it }
        .map { maps["temperature-to-humidity"]!![it] ?: it }
        .map { maps["humidity-to-location"]!![it] ?: it }
        .toList()

    println(locationList)
    return locationList
}
