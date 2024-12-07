package syh.year2023.day13

import syh.AbstractAocDay


class Puzzle13 : AbstractAocDay(2023, 13) {
    private val ROCKS = "#"
    private val ASH = "."
    override fun doA(file: String): Long {
        val patterns = readDoubleLineFile(file)
            .map { readPattern(it) }

        println()
        return calculateSum(patterns, "part a")
    }

    override fun doB(file: String): Long {
        val patterns = readDoubleLineFile(file)
            .map { readPattern(it) }
        val alternativePatterns = patterns.map { findAlternativePattern(it) }

        println()
        return calculateSum(alternativePatterns, "part b")
    }

    private fun calculateSum(patterns: List<Pattern>, partStr: String): Long {
        val verticalSum = patterns.filter { it.reflectionType == ReflectionType.VERTICAL }
            .sumOf { it.reflectionIndex + 1 }
        val horizontalSum = patterns.filter { it.reflectionType == ReflectionType.HORIZONTAL }
            .sumOf { (it.reflectionIndex + 1) * 100 }

        val sum = verticalSum + horizontalSum
        println("vertical sum: $verticalSum")
        println("horizontal sum: $horizontalSum")
        println("total for $partStr: $sum")
        return sum.toLong()
    }

    private fun readPattern(str: String): Pattern {
        val grid = str.split("\r\n")
            .map { it.split("").filter { itt -> itt.isNotEmpty() }.toMutableList() }
            .toMutableList()

        val horizontalReflectionIndex = findHorizontalReflection(grid)
        if (horizontalReflectionIndex != -1) {
            return Pattern(grid, ReflectionType.HORIZONTAL, horizontalReflectionIndex)
        }

        val verticalReflectionIndex = findVerticalReflection(grid)
        if (verticalReflectionIndex != -1) {
            return Pattern(grid, ReflectionType.VERTICAL, verticalReflectionIndex)
        }

        throw IllegalStateException("could not find pattern in \n $grid")
    }

    private fun findAlternativePattern(pattern: Pattern): Pattern {
        val newGrid = pattern.grid.map { it.toMutableList() }.toMutableList()
        val oldReflectionType = pattern.reflectionType
        val oldReflectionIndex = pattern.reflectionIndex

        println("Finding alternative pattern for found pattern $oldReflectionType on index $oldReflectionIndex")

        for (i in newGrid.indices) {
            for (j in newGrid[i].indices) {

                val oldValue = newGrid[i][j]
                val newValue = if (oldValue == ROCKS) ASH else ROCKS
                newGrid[i][j] = newValue

                for (jj in 0..newGrid[i].size - 2) {
                    if (oldReflectionType == ReflectionType.VERTICAL && oldReflectionIndex == jj) {
                        continue
                    }
                    val possibleVerticalReflection = tryVerticalReflection(newGrid, jj)
                    if (possibleVerticalReflection) {
                        println("found vertical reflection at $jj")
                        return Pattern(newGrid, ReflectionType.VERTICAL, jj)
                    }
                }

                for (ii in 0..newGrid.size - 2) {
                    if (oldReflectionType == ReflectionType.HORIZONTAL && oldReflectionIndex == ii) {
                        continue
                    }
                    val possibleHorizontalReflection = tryHorizontalReflection(newGrid, ii)
                    if (possibleHorizontalReflection) {
                        println("found horizontal reflection at $ii")
                        return Pattern(newGrid, ReflectionType.HORIZONTAL, ii)
                    }
                }

                newGrid[i][j] = oldValue
            }
        }

        prettyPrint(pattern.grid)
        throw IllegalStateException("could not find pattern in new grid")
    }

    private fun prettyPrint(grid: List<List<String>>) {
        grid.forEach { println(it.joinToString("") { itt -> itt }) }
    }

    private fun findVerticalReflection(grid: List<List<String>>): Int {
        for (j in 0..grid[0].size - 2) {
            val found = tryVerticalReflection(grid, j)
            if (found) {
                prettyPrint(grid)
                println("found vertical reflection at $j")
                return j
            }
        }
        return -1
    }

    private fun tryVerticalReflection(grid: List<List<String>>, verticalReflectionIndex: Int): Boolean {
        var indexLeft = verticalReflectionIndex
        var indexRight = verticalReflectionIndex + 1

        while (indexLeft in grid[0].indices && indexRight in grid[0].indices) {

            for (i in grid.indices) {
                if (grid[i][indexLeft] != grid[i][indexRight]) {
                    return false
                }
            }

            indexLeft--
            indexRight++
        }
        return true
    }

    private fun findHorizontalReflection(grid: List<List<String>>): Int {
        for (i in 0..grid.size - 2) {
            val found = tryHorizontalReflection(grid, i)
            if (found) {
                prettyPrint(grid)
                println("found horizontal reflection at $i")
                return i
            }
        }
        return -1
    }

    private fun tryHorizontalReflection(grid: List<List<String>>, horizontalReflectionIndex: Int): Boolean {
        var indexUp = horizontalReflectionIndex
        var indexDown = horizontalReflectionIndex + 1

        while (indexUp in grid.indices && indexDown in grid.indices) {
            if (grid[indexUp] != grid[indexDown]) {
                return false
            }
            indexUp--
            indexDown++
        }
        return true
    }

    enum class ReflectionType { HORIZONTAL, VERTICAL }
    data class Pattern(val grid: List<List<String>>, val reflectionType: ReflectionType, val reflectionIndex: Int)

}