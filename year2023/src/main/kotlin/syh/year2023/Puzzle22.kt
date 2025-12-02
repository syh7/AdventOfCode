package syh.year2023

import java.util.SortedSet
import kotlin.math.max
import syh.library.AbstractAocDay


class Puzzle22 : AbstractAocDay(2023, 22) {

    private fun readFallingBricks(file: String): SortedSet<Brick> {
        val bricks: SortedSet<Brick> = readSingleLineFile(file)
            .mapIndexed { index, str -> mapToBrick(str, index) }
            .toSortedSet()

//        println("bricks after reading")
//        bricks.forEach { println(it) }

        makeBricksFall(bricks)

        println("bricks after falling:")
        bricks.forEach { println(it) }

        return bricks
    }

    override fun doA(file: String): String {
        val bricks = readFallingBricks(file)

        // .all{} returns true if list is empty
        // so this checks both bricks that are not supporting anything, or bricks that are supporting more than 1 brick
        return bricks.filter { it.supporting.all { supported -> supported.supportedBy.size > 1 } }.size.toString()
    }

    override fun doB(file: String): String {
        val bricks = readFallingBricks(file)
        val higherBricks = bricks.filterNot { it.start.z == 1 }.toSortedSet()

        return bricks.sumOf {
            val value = calculateDisintegratedBricks(it, higherBricks)
//            println("brick ${it.identifier} would disintegrate $value other bricks")
            value
        }.toString()
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

    private fun makeBricksFall(bricks: SortedSet<Brick>) {

        val fallenBricks = mutableSetOf<Brick>()
        var highestZ = 1

        for (brick in bricks) {
            // set to current highest z point to prevent needless moving and checking from high z to lower empty z's
            brick.moveTo(highestZ + 1)

            var moveDown = true

            while (moveDown) {
                brick.move(-1)

                for (checkBrick in fallenBricks) {
                    // prevent unnecessary checking
                    if (checkBrick.end.z < brick.start.z) {
                        continue
                    }

                    if (brick.intersects(checkBrick)) {
//                        println("brick $brick intersects with $checkBrick")
                        moveDown = false
                        brick.supportedBy.add(checkBrick)
                        checkBrick.supporting.add(brick)
                    }

                }
                if (!moveDown) {
                    brick.move(1)
                }
                if (brick.start.z == 1) {
                    break
                }
            }

            fallenBricks.add(brick)
            highestZ = max(highestZ, brick.end.z)
        }
    }

    private fun mapToBrick(str: String, index: Int): Brick {
        val (start, end) = str.split("~")
        val (startX, startY, startZ) = start.split(",").map { it.toInt() }
        val (endX, endY, endZ) = end.split(",").map { it.toInt() }

        return Brick(index, Coord3D(startX, startY, startZ), Coord3D(endX, endY, endZ))
    }

    data class Brick(
        val identifier: Int,
        val start: Coord3D,
        val end: Coord3D,
        val supporting: MutableSet<Brick> = mutableSetOf(),
        val supportedBy: MutableSet<Brick> = mutableSetOf()
    ) : Comparable<Brick> {

        fun move(offset: Int) {
            start.z += offset
            end.z += offset
        }

        fun moveTo(newStartZ: Int) {
            val diff = end.z - start.z
            start.z = newStartZ
            end.z = newStartZ + diff
        }

        fun intersects(other: Brick): Boolean {
            return (this.start.x in other.start.x..other.end.x
                    || this.end.x in other.start.x..other.end.x
                    || other.start.x in this.start.x..this.end.x
                    || other.end.x in this.start.x..this.end.x)
                    && (this.start.y in other.start.y..other.end.y
                    || this.end.y in other.start.y..other.end.y
                    || other.start.y in this.start.y..this.end.y
                    || other.end.y in this.start.y..this.end.y)
                    && (this.start.z in other.start.z..other.end.z
                    || this.end.z in other.start.z..other.end.z
                    || other.start.z in this.start.z..this.end.z
                    || other.end.z in this.start.z..this.end.z)
        }

        override fun compareTo(other: Brick): Int {
            return this.start.compareTo(other.start)
        }

        override fun toString(): String {
            return "{identifier=$identifier, start=$start, end=$end, supporting=${supporting.map { it.identifier }}, supportedBy=${supportedBy.map { it.identifier }}}}"
        }

        override fun hashCode(): Int {
            return identifier
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return identifier == (other as Brick).identifier
        }
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
