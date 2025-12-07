package syh.year2021

import syh.library.AbstractAocDay

class Puzzle12 : AbstractAocDay(2021, 12) {
    override fun doA(file: String): String {
        val paths = readSingleLineFile(file).map {
            val (start, end) = it.split("-")
            start to end
        }
        val cavePaths = mutableMapOf<String, List<String>>()
        for (path in paths) {
            cavePaths.merge(path.first, listOf(path.second)) { list, new -> list + new }
            cavePaths.merge(path.second, listOf(path.first)) { list, new -> list + new }
        }

        return travelA("start", setOf("start"), cavePaths).toString()
    }

    override fun doB(file: String): String {
        val paths = readSingleLineFile(file).map {
            val (start, end) = it.split("-")
            start to end
        }
        val cavePaths = mutableMapOf<String, List<String>>()
        for (path in paths) {
            cavePaths.merge(path.first, listOf(path.second)) { list, new -> list + new }
            cavePaths.merge(path.second, listOf(path.first)) { list, new -> list + new }
        }

        return travelB("start", setOf("start"), false, cavePaths).toString()
    }

    private fun travelA(currentCave: String, seen: Set<String>, paths: Map<String, List<String>>): Int {
        if (currentCave == "end") {
//            println("found path: ${seen.joinToString()}")
            return 1
        }
        val newCaves = paths[currentCave]?.filter { it[0].isUpperCase() || it !in seen }
        if (newCaves.isNullOrEmpty()) {
            return 0
        }
        return newCaves.sumOf {
            travelA(it, seen + it, paths)
        }

    }

    private fun travelB(currentCave: String, seen: Set<String>, smallTwice: Boolean, paths: Map<String, List<String>>): Int {
        if (currentCave == "end") {
//            println("found path: ${seen.joinToString()}")
            return 1
        }
        val newCaves = paths[currentCave]?.filter { it[0].isUpperCase() || it !in seen || (!smallTwice && it != "start") }
        if (newCaves.isNullOrEmpty()) {
            return 0
        }
        return newCaves.sumOf {
            val newTwice = smallTwice || (it[0].isLowerCase() && it in seen)
            travelB(it, seen + it, newTwice, paths)
        }

    }

}