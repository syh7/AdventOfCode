package syh.year2022

import kotlin.math.max
import kotlin.math.min
import syh.library.AbstractAocDay

class Puzzle14 : AbstractAocDay(2022, 14) {

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        var maxY = 0
        val grid = MutableList(1000) { MutableList(1000) { false } }

        for (line in lines) {
            val tempMaxY = addWall(line, grid)
            maxY = max(maxY, tempMaxY)
        }

        var steps = 0
        while (makeSandFall(grid, maxY)) {
            steps++
        }

        println("total steps: $steps")
        return steps.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        var maxY = 0
        val grid = MutableList(1000) { MutableList(1000) { false } }

        for (line in lines) {
            val tempMaxY = addWall(line, grid)
            maxY = max(maxY, tempMaxY)
        }

        val bottomFloor = "0,${maxY + 2} -> 999,${maxY + 2}"
        addWall(bottomFloor, grid)

        var steps = 0
        while (makeSandFall(grid, maxY)) {
            steps++
        }

        println("total steps: $steps")
        return steps.toString()
    }

    private fun addWall(line: String, grid: MutableList<MutableList<Boolean>>): Int {
        var maxY = 0
        val coords = line.split(" -> ")
        for (i in 1..<coords.size) {
            val x1 = coords[i - 1].split(",")[0].toInt()
            val y1 = coords[i - 1].split(",")[1].toInt()
            val x2 = coords[i].split(",")[0].toInt()
            val y2 = coords[i].split(",")[1].toInt()
            for (j in min(x1, x2)..max(x1, x2)) {
                for (k in min(y1, y2)..max(y1, y2)) {
                    grid[j][k] = true
                }
            }
            maxY = max(maxY, max(y1, y2))
        }
        return maxY
    }

    private fun makeSandFall(grid: MutableList<MutableList<Boolean>>, maxY: Int): Boolean {
        if (grid[500][0]) {
            return false
        }

        var x = 500
        var y = 0
        while (y <= maxY + 3) {
            if (!grid[x][y + 1]) {
                y += 1
                continue
            }
            if (!grid[x - 1][y + 1]) {
                y += 1
                x -= 1
                continue
            }
            if (!grid[x + 1][y + 1]) {
                y += 1
                x += 1
                continue
            }
            grid[x][y] = true
            return true
        }
        return false
    }
}
