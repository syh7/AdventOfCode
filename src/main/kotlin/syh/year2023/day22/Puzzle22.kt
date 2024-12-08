package syh.year2023.day22

import syh.AbstractAocDay
import java.util.*


class Puzzle22 : AbstractAocDay(2023, 22) {

    private fun readFallingBricks(file: String): SortedSet<Brick> {
        val bricks: SortedSet<Brick> = readSingleLineFile(file)
            .mapIndexed { index, str -> mapToBrick(str, index) }
            .toSortedSet()

        bricks.forEach { println(it) }

        bricks.forEach { makeBrickFall(it, bricks) }
        fillSupportingBricks(bricks)
        return bricks
    }

    override fun doA(file: String): Long {
        val bricks = readFallingBricks(file)
        val notSupportingBricks = bricks.filter { it.supporting.isEmpty() }
        val doubleSupportingBricks = bricks.filter { it.supporting.all { supported -> supported.supportedBy.size > 1 } }

        println("not supporting bricks:")
        notSupportingBricks.forEach { println(it) }
        println("double supporting bricks:")
        doubleSupportingBricks.forEach { println(it) }

        val resultSet = mutableSetOf<Brick>()
        resultSet.addAll(notSupportingBricks)
        resultSet.addAll(doubleSupportingBricks)
        println("resultSet:")
        resultSet.forEach { println(it) }

        return resultSet.size.toLong()
    }

    override fun doB(file: String): Long {
        val bricks = readFallingBricks(file)
        val higherBricks = bricks.filterNot { it.coords.any { coord -> coord.z == 1 } }.toSortedSet()

        return bricks.sumOf {
            val value = calculateDisintegratedBricks(it, higherBricks)
            println("brick ${it.identifier} would disintegrate $value other bricks")
            value
        }.toLong()
    }

    private fun calculateDisintegratedBricks(startBrick: Brick, bricks: SortedSet<Brick>): Int {
        val supportedMap = bricks.associateWith { it.supportedBy.size }.toMutableMap()

        var disintegrated = 0
        val disintegratingBricks = mutableSetOf(startBrick)
        
        while (disintegratingBricks.isNotEmpty()) {
            disintegrated++
            val brick = disintegratingBricks.first().also { disintegratingBricks.remove(it) }
            for (supported in brick.supporting) {
                supportedMap[supported] = supportedMap[supported]!! - 1
                if (supportedMap[supported] == 0) {
                    disintegratingBricks.add(supported)
                }
            }
        }

        return disintegrated - 1 // because initial brick does not count
    }

    private fun fillSupportingBricks(bricks: SortedSet<Brick>) {
        for (brick in bricks) {
            val copySet = bricks.toSortedSet()
            copySet.remove(brick)

            val lowerConnectingBricks = findIntersectingBricks(brick, copySet, -1)
            val higherConnectingBricks = findIntersectingBricks(brick, copySet, +1)

            brick.supporting.addAll(higherConnectingBricks)
            brick.supportedBy.addAll(lowerConnectingBricks)
        }
    }

    private fun makeBrickFall(brick: Brick, otherBricks: Set<Brick>) {
        val bricksCopy = otherBricks.toMutableSet()
        bricksCopy.remove(brick)

//        println()
//        println("lowering brick $brick")
        while (brick.coords.first().z > 1) {
            var intersects = false

            coordloop@ for (coord in brick.coords) {
                intersects = findIntersectingBricks(brick, bricksCopy, -1).isNotEmpty()
                if (intersects) break@coordloop
            }

            if (intersects) {
                println("brick would intersect, so stop falling at z=${brick.coords.first().z}")
                break
            }

            brick.coords.forEach { it.lower() }
        }
    }

    private fun findIntersectingBricks(brick: Brick, otherBricks: Set<Brick>, zOffset: Int): MutableSet<Brick> {
        val allBricks = mutableSetOf<Brick>()
        for (coord in brick.coords) {
            val lowerCoord = Coord3D(coord.x, coord.y, coord.z + zOffset)
//            println("created changed coord of original coord $coord : $lowerCoord")

            val intersectingBricks = otherBricks.filter { it.coords.contains(lowerCoord) }
//            intersectingBricks.forEach { println(" - intersects with brick $it") }

            allBricks.addAll(intersectingBricks)
        }
        return allBricks
    }

    private fun mapToBrick(str: String, index: Int): Brick {
        val (start, end) = str.split("~")
        val (startX, startY, startZ) = start.split(",").map { it.toInt() }
        val (endX, endY, endZ) = end.split(",").map { it.toInt() }

        val startCoord = Coord3D(startX, startY, startZ)
        if (startX == endX && startY == endY && startZ == endZ) {
            return Brick(index, sortedSetOf(startCoord))
        }
        if (startX != endX) {
            val coords = IntRange(startX, endX).map { Coord3D(it, startY, startZ) }
            return Brick(index, coords.toSortedSet())
        }
        if (startY != endY) {
            val coords = IntRange(startY, endY).map { Coord3D(startX, it, startZ) }
            return Brick(index, coords.toSortedSet())
        }

        // then Z must be different
        // because of logic of the puzzle but also because the of the first if checking all coord values
        val coords = IntRange(startZ, endZ).map { Coord3D(startX, startY, it) }
        return Brick(index, coords.toSortedSet())

    }

    data class Brick(
        val identifier: Int,
        val coords: SortedSet<Coord3D>,
        val supporting: MutableSet<Brick> = mutableSetOf(),
        val supportedBy: MutableSet<Brick> = mutableSetOf()
    ) : Comparable<Brick> {
        override fun compareTo(other: Brick): Int {
            return this.coords.first().compareTo(other.coords.first())
        }

        override fun toString(): String {
            return "{identifier=$identifier, coords=${coords.joinToString()}, supporting=${supporting.map { it.identifier }}, supportedBy=${supportedBy.map { it.identifier }}}}"
        }

        override fun hashCode(): Int {
            return identifier
        }
    }

    data class Coord3D(val x: Int, val y: Int, var z: Int) : Comparable<Coord3D> {
        fun lower() {
            this.z -= 1
        }

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
