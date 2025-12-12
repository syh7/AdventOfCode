package syh.year2025

import syh.library.AbstractAocDay

class Puzzle11 : AbstractAocDay(2025, 11) {
    override fun doA(file: String): String {
        val map = readMap(file)
        val paths = dfs(map, "you", "out", mutableMapOf())
        return paths.toString()
    }

    override fun doB(file: String): String {
        val map = readMap(file)

        val dacToFft = dfs(map, "dac", "fft", mutableMapOf())

        if (dacToFft != 0L) {
            val startToDac = dfs(map, "svr", "dac", mutableMapOf())
            val fftToEnd = dfs(map, "fft", "out", mutableMapOf())
            return (startToDac * dacToFft * fftToEnd).toString()
        } else {
            val startToFFt = dfs(map, "svr", "fft", mutableMapOf())
            val fftToDac = dfs(map, "fft", "dac", mutableMapOf())
            val dacToEnd = dfs(map, "dac", "out", mutableMapOf())

            return (startToFFt * dacToEnd * fftToDac).toString()
        }
    }

    private fun dfs(map: Map<String, List<String>>, start: String, end: String, cache: MutableMap<String, Long>): Long {
//        println("going from $start -> $end")
        if (start == end) {
            return 1
        }
        if (cache.containsKey(start)) {
            return cache[start]!!
        }

        val nextPaths = map[start]?.sumOf { neighbour ->
            dfs(map, neighbour, end, cache)
        }
        cache[start] = nextPaths ?: 0
        return cache[start]!!
    }

    private fun readMap(file: String): Map<String, List<String>> {
        val map = readSingleLineFile(file).associate {
            val (from, tos) = it.split(": ")
            from to tos.split(" ")
        }
        map.forEach { println(it) }
        return map
    }
}