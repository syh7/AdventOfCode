package syh.year2022

import java.util.Stack
import syh.library.AbstractAocDay


class Puzzle18 : AbstractAocDay(2022, 18) {

    override fun doA(file: String): String {
        val blocks = readSingleLineFile(file).map { parseCoord(it) }
        val openSides = blocks.sumOf { findNeighbours(it).count { neighbour -> neighbour !in blocks } }
        println("total open sides: $openSides")
        return openSides.toString()
    }

    override fun doB(file: String): String {
        val blocks = readSingleLineFile(file).map { parseCoord(it) }.toSet()

        val highestX = blocks.maxOf { it.x }
        val highestY = blocks.maxOf { it.y }
        val highestZ = blocks.maxOf { it.z }
        val lowestX = blocks.minOf { it.x }
        val lowestY = blocks.minOf { it.y }
        val lowestZ = blocks.minOf { it.z }

        val xRange = lowestX - 2..highestX + 2
        val yRange = lowestY - 2..highestY + 2
        val zRange = lowestZ - 2..highestZ + 2

        val seen = mutableSetOf<Coord3D>()
        val stack = Stack<Coord3D>()
        stack.add(Coord3D(-1, -1, -1))

        while (stack.isNotEmpty()) {
            val coord = stack.pop()

            if (coord in seen) continue
            if (coord in blocks) continue
            if (coord.x !in xRange || coord.y !in yRange || coord.z !in zRange) continue

            seen.add(coord)
            stack.addAll(findNeighbours(coord))
        }

        val total = blocks.sumOf { findNeighbours(it).count { neighbour -> neighbour in seen } }
        return total.toString()
    }

    private fun findNeighbours(block: Coord3D): Set<Coord3D> {
        val neighbours = mutableSetOf<Coord3D>()

        neighbours.add(Coord3D(block.x - 1, block.y, block.z))
        neighbours.add(Coord3D(block.x + 1, block.y, block.z))
        neighbours.add(Coord3D(block.x, block.y - 1, block.z))
        neighbours.add(Coord3D(block.x, block.y + 1, block.z))
        neighbours.add(Coord3D(block.x, block.y, block.z - 1))
        neighbours.add(Coord3D(block.x, block.y, block.z + 1))

        return neighbours
    }

    private fun parseCoord(str: String): Coord3D {
        val (x, y, z) = str.split(",").map { it.toInt() }
        return Coord3D(x, y, z)
    }

    data class Coord3D(val x: Int, val y: Int, var z: Int) : Comparable<Coord3D> {
        override fun compareTo(other: Coord3D): Int {
            if (this.z != other.z) {
                return this.z.compareTo(other.z)
            }
            if (this.x != other.x) {
                return this.x.compareTo(other.x)
            }
            if (this.y != other.y) {
                return this.y.compareTo(other.y)
            }
            return 0
        }
    }
}
