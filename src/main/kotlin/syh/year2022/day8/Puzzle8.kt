package syh.year2022.day8

import syh.readSingleLineFile

data class Tree(val height: Int, var visible: Boolean = false, var scenic: Int = 0)

fun main() {
    val lines = readSingleLineFile("year2022/day8/actual.txt")

    val grid = mutableListOf<MutableList<Tree>>()
    for (line in lines) {
        val row = mutableListOf<Tree>()
        for (charIndex in line.indices) {
            row.add(Tree(line[charIndex].toString().toInt()))
        }
        grid.add(row)
    }

    grid.forEach { println(it) }

    val gridHeight = grid.size
    val gridWidth = grid[0].size

    setVisibility(grid, gridHeight, gridWidth)
    val visible = grid.sumOf { it.count { tree -> tree.visible } }
    println("amount of trees visible: $visible")

    setScenic(grid, gridHeight, gridWidth)
    val maxScenic = grid.maxOf { it.maxOf { tree -> tree.scenic } }
    println("max scenic value: $maxScenic")
}

private fun setVisibility(
    grid: List<MutableList<Tree>>,
    gridHeight: Int,
    gridWidth: Int
) {
    grid.first().forEach { it.visible = true }
    grid.last().forEach { it.visible = true }
    grid.forEach { it.first().visible = true }
    grid.forEach { it.last().visible = true }

    for (i in 1 until gridHeight) {
        var highestHeight = grid[i][0].height
        for (j in 1 until gridWidth) {
            if (grid[i][j].height > highestHeight) {
                grid[i][j].visible = true;
                highestHeight = grid[i][j].height
                if (highestHeight == 9)
                    break
            }
        }
        highestHeight = grid[i][gridWidth - 1].height
        for (j in gridWidth - 2 downTo 1) {
            if (grid[i][j].height > highestHeight) {
                grid[i][j].visible = true;
                highestHeight = grid[i][j].height
                if (highestHeight == 9)
                    break
            }
        }
    }

    for (j in 1 until gridWidth) {
        var highestHeight = grid[0][j].height
        for (i in 1 until gridHeight) {
            if (grid[i][j].height > highestHeight) {
                grid[i][j].visible = true;
                highestHeight = grid[i][j].height
                if (highestHeight == 9)
                    break
            }
        }
        highestHeight = grid[gridHeight - 1][j].height
        for (i in gridHeight - 2 downTo 1) {
            if (grid[i][j].height > highestHeight) {
                grid[i][j].visible = true;
                highestHeight = grid[i][j].height
                if (highestHeight == 9)
                    break
            }
        }
    }
}


private fun setScenic(
    grid: List<MutableList<Tree>>,
    gridHeight: Int,
    gridWidth: Int
) {
    // edges are always 0 because one side will be 0, so do not check them
    for (i in 1 until gridHeight - 1) {
        for (j in 1 until gridWidth - 1) {
            calculateScenic(grid, gridHeight, gridWidth, i, j)
        }
    }
}

fun calculateScenic(grid: List<MutableList<Tree>>, gridHeight: Int, gridWidth: Int, x: Int, y: Int) {
    val treeHeight = grid[x][y].height

    var scenicDown = 0
    for (i in x + 1 until gridHeight) {
        scenicDown++
        if (grid[i][y].height >= treeHeight) {
            break
        }
    }

    var scenicUp = 0
    for (i in x - 1 downTo 0) {
        scenicUp++
        if (grid[i][y].height >= treeHeight) {
            break
        }
    }

    var scenicRight = 0
    for (j in y + 1 until gridWidth) {
        scenicRight++
        if (grid[x][j].height >= treeHeight) {
            break
        }
    }

    var scenicLeft = 0
    for (j in y - 1 downTo 0) {
        scenicLeft++
        if (grid[x][j].height >= treeHeight) {
            break
        }
    }

    val scenic = scenicRight * scenicLeft * scenicUp * scenicDown
    grid[x][y].scenic = scenic
    println("set tree [$x][$y] scenic to $scenic")
    println()
}
