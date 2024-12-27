package syh.year2024.day25

import syh.AbstractAocDay


class Puzzle25 : AbstractAocDay(2024, 25) {
    override fun doA(file: String): String {
        val split = readDoubleLineFile(file)
        val (lockStr, keyStr) = split.partition { it.startsWith("#####") }
        val locks = lockStr.map { parseLock(it) }
        val keys = keyStr.map { parseLock(it) }

//        println("locks: $locks")
//        println("keys: $keys")

        var validCombos = 0

        for (lock in locks) {
            for (key in keys) {
                var valid = true
                for (i in lock.indices) {
                    if (lock[i] + key[i] > 6) {
                        valid = false
                    }
                }
                if (valid) {
//                    println("found valid combination of lock $lock and key $key")
                    validCombos += 1
                }
            }
        }

        println("total valid combinations = $validCombos")

        return validCombos.toString()
    }

    private fun parseLock(string: String): MutableList<Int> {
        val grid = string.split("\r\n")
            .map { it.split("").filter { c -> c.isNotEmpty() } }
        val lockValues = MutableList(5) { 0 }
        for (row in 1..<grid.size) {
            for (column in 0..<5) {
                if (grid[row][column] == "#") {
                    lockValues[column] = lockValues[column] + 1
                }
            }
        }
        return lockValues
    }

    private fun parseKey(string: String): MutableList<Int> {
        val grid = string.split("\r\n")
            .map { it.split("").filter { c -> c.isNotEmpty() } }
        val lockValues = MutableList(5) { 0 }
        for (row in grid.size - 2 downTo 1) {
            for (column in 0..<5) {
                if (grid[row][column] == "#") {
                    lockValues[column] = lockValues[column] + 1
                }
            }
        }
        return lockValues
    }

    override fun doB(file: String): String {
        return ""
    }
}
