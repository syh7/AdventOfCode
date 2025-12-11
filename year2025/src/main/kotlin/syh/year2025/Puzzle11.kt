package syh.year2025

import syh.library.AbstractAocDay

class Puzzle11 : AbstractAocDay(2025, 11) {
    override fun doA(file: String): String {
        val map = readMap(file)
        val paths = dfs(map, "you", "out", listOf("you"))
            .filter { it.isNotEmpty() }
        return paths.size.toString()
    }

    override fun doB(file: String): String {
        val map = readMap(file)

        val dacToFft = dfs(map, "dac", "fft", listOf("dac")).filter { it.isNotEmpty() }

        if (dacToFft.isNotEmpty()) {
            val startToDac = dfs(map, "svr", "dac", listOf("svr")).filter { it.isNotEmpty() }
            val fftToEnd = dfs(map, "fft", "out", listOf("fft")).filter { it.isNotEmpty() }
            return (startToDac.size * dacToFft.size * fftToEnd.size).toString()
        } else {
            val startToFFt = dfs(map, "svr", "fft", listOf("svr")).filter { it.isNotEmpty() }
            val fftToDac = dfs(map, "fft", "dac", listOf("fft")).filter { it.isNotEmpty() }
            val dacToEnd = dfs(map, "dac", "out", listOf("dac")).filter { it.isNotEmpty() }

            return (startToFFt.size * dacToEnd.size * fftToDac.size).toString()
        }
        throw IllegalStateException("help")
    }

    private fun dfs(map: Map<String, List<String>>, start: String, end: String, path: List<String>): List<List<String>> {
//        println("going from $start -> $end")
        if (start == end) {
            return listOf(path)
        }

        return map[start]?.flatMap { dfs(map, it, end, path + it) } ?: emptyList()
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