package syh.year2023.day14

import syh.AbstractAocDay


typealias Grid = MutableList<MutableList<String>>

class Puzzle14 : AbstractAocDay(2023, 14) {
    private val WALL = "#"
    private val BOULDER = "O"
    private val EMPTY = "."

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val grid = mutableListOf<MutableList<String>>()

        for (i in lines.indices) {
            val line = lines[i]
            val list = line.split("")
                .filter { it.isNotEmpty() }
                .toMutableList()
            grid.add(list)
        }

        val northTiltLoad = calculateLoadAfterSingleNorthTile(cloneGrid(grid))
        println("load on grid after single north tile: $northTiltLoad")

        return northTiltLoad.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val grid = mutableListOf<MutableList<String>>()

        for (i in lines.indices) {
            val line = lines[i]
            val list = line.split("")
                .filter { it.isNotEmpty() }
                .toMutableList()
            grid.add(list)
        }

        val cycleLoad = calculateLoadAfterCycles(cloneGrid(grid))
        println("total load on grid: $cycleLoad")
        return cycleLoad.toString()
    }


    private fun calculateLoadAfterSingleNorthTile(grid: Grid): Int {
        tiltNorth(grid)
        return calculateLoad(grid)
    }


    private fun calculateLoadAfterCycles(grid: Grid): Int {
        val gridCycleMap = hashMapOf<Grid, Int>()

        val cycleStart: Int
        var cycleEnd = 0
        var cycleLength = 0

        for (i in 0..<1000000000) {
            if (gridCycleMap.contains(grid)) {
                cycleStart = gridCycleMap[grid]!!
                cycleEnd = i
                cycleLength = cycleEnd - cycleStart
                break

            } else {
                val before = cloneGrid(grid)
                gridCycleMap[before] = i
                cycleGrid(grid)
            }
        }

        val leftoverSteps = (1000000000 - cycleEnd) % cycleLength

//    println("Found cycle from $cycleStart to $cycleEnd with length $cycleLength")
//    println("leftover steps: $leftoverSteps")

        for (i in 0..<leftoverSteps) {
            cycleGrid(grid)
        }

        return calculateLoad(grid)
    }

    private fun cloneGrid(grid: Grid): Grid {
        val newGrid = mutableListOf<MutableList<String>>()
        for (currentLine in grid) {
            val newLine = mutableListOf<String>()
            for (str in currentLine) {
                newLine.add(str)
            }
            newGrid.add(newLine)
        }
        return newGrid
    }

    private fun cycleGrid(grid: Grid) {
        tiltNorth(grid)
        tiltWest(grid)
        tiltSouth(grid)
        tiltEast(grid)
    }

    private fun calculateLoad(grid: Grid): Int {
        var totalLoad = 0
        for (i in 0..<grid.size) {
            val lineValue = grid.size - i
            val boulders = grid[i].count { it == BOULDER }
            val load = lineValue * boulders
            totalLoad += load
//        println("Found $boulders boulders worth $lineValue each, totalling $load")
        }
        return totalLoad
    }

    private fun tiltNorth(grid: Grid) {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == BOULDER) {
//                println("found boulder at [$i][$j], going north")
                    tryToMoveBoulderNorth(grid, i, j)
                }
            }
        }
    }

    private fun tryToMoveBoulderNorth(grid: Grid, startI: Int, j: Int) {
        if (startI == 0) {
            return
        }
        for (i in startI downTo 1) {
//        println("checking if [$i][$j] can move north to [${i - 1}][$j]")

            if (grid[i - 1][j] == EMPTY) {
                grid[i - 1][j] = BOULDER
                grid[i][j] = EMPTY
//            println("moved boulder [$i][$j] to [${i - 1}][$j]")

            } else {
                break
            }
        }
    }

    private fun tiltSouth(grid: Grid) {
        for (i in grid.size - 1 downTo 0) {
            for (j in grid[i].indices) {
                if (grid[i][j] == BOULDER) {
//                println("found boulder at [$i][$j], going south")
                    tryToMoveBoulderSouth(grid, i, j)
                }
            }
        }
    }

    private fun tryToMoveBoulderSouth(grid: Grid, startI: Int, j: Int) {
        if (startI == grid.size - 1) {
            return
        }
        for (i in startI..<grid.size - 1) {
//        println("checking if [$i][$j] can move north to [${i - 1}][$j]")

            if (grid[i + 1][j] == EMPTY) {
                grid[i + 1][j] = BOULDER
                grid[i][j] = EMPTY
//            println("moved boulder [$i][$j] to [${i - 1}][$j]")

            } else {
                break
            }
        }
    }

    private fun tiltWest(grid: Grid) {
        for (j in grid[0].indices) {
            for (i in grid.indices) {
                if (grid[i][j] == BOULDER) {
//                println("found boulder at [$i][$j], going west")
                    tryToMoveBoulderWest(grid, i, j)
                }
            }
        }
    }

    private fun tryToMoveBoulderWest(grid: Grid, i: Int, startJ: Int) {
        if (startJ == 0) {
            return
        }
        for (j in startJ downTo 1) {
            if (grid[i][j - 1] == EMPTY) {
                grid[i][j - 1] = BOULDER
                grid[i][j] = EMPTY
            } else {
                break
            }
        }
    }

    private fun tiltEast(grid: Grid) {
        for (j in grid[0].size - 1 downTo 0) {
            for (i in grid.indices) {
                if (grid[i][j] == BOULDER) {
//                println("found boulder at [$i][$j], going east")
                    tryToMoveBoulderEast(grid, i, j)
                }
            }
        }
    }

    private fun tryToMoveBoulderEast(grid: Grid, i: Int, startJ: Int) {
        if (startJ == grid.size - 1) {
            return
        }
        for (j in startJ..<grid[i].size - 1) {
            if (grid[i][j + 1] == EMPTY) {
                grid[i][j + 1] = BOULDER
                grid[i][j] = EMPTY
            } else {
                break
            }
        }
    }
}
