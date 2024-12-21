package syh.year2023.day24

import java.math.BigDecimal
import syh.AbstractAocDay

/**
 * all math courtesy of reddit
 * https://www.reddit.com/r/adventofcode/comments/18pnycy/2023_day_24_solutions/?sort=top
 */
class Puzzle24 : AbstractAocDay(2023, 24) {
    override fun doA(file: String): String {
        val min = if (file == "test") BigDecimal.valueOf(7) else BigDecimal.valueOf(200000000000000)
        val max = if (file == "test") BigDecimal.valueOf(27) else BigDecimal.valueOf(400000000000000)


        val hailstones = readSingleLineFile(file)
            .map { Hailstone.parseLine(it) }

        var intersections = 0

        for (i in hailstones.indices) {
            for (j in i..<hailstones.size) {

                val first = hailstones[i]
                val second = hailstones[j]

//                println("testing first $first")
//                println("testing second $second")

                val denom = (first.velocity.x * second.velocity.y) - (first.velocity.y * second.velocity.x)
                val numer1 = ((second.point.x - first.point.x) * second.velocity.y) - ((second.point.y - first.point.y) * second.velocity.x)
                val numer2 = ((first.point.x - second.point.x) * first.velocity.y) - ((first.point.y - second.point.y) * first.velocity.x)
                if ((denom != BigDecimal.ZERO && numer1 / denom > BigDecimal.ZERO) && (numer2 / denom) < BigDecimal.ZERO) {
                    val intersectionX = (numer1 / denom) * first.velocity.x + first.point.x
                    val intersectionY = (numer1 / denom) * first.velocity.y + first.point.y
                    if (intersectionX > min && intersectionX < max && intersectionY > min && intersectionY < max) {
//                        println("hailstones $first and $second intersect at [$intersectionX][$intersectionY]")
                        intersections++
                    }
                }
            }

        }

        return intersections.toString()
    }

    override fun doB(file: String): String {
        val hailstones = readSingleLineFile(file)
            .map { Hailstone.parseLine(it) }

        var firstHailstone = hailstones[0]
        var secondHailstone: Hailstone? = null
        var thirdHailstone: Hailstone? = null

        var i = 1
        while (i in 1..<hailstones.size) {
            if (isIndependent(firstHailstone, hailstones[i])) {
                secondHailstone = hailstones[i]
                println("found independent second hailstone $i: $secondHailstone")
                break
            }
            i++
        }
        if (secondHailstone == null) {
            throw IllegalStateException("could not find independent second hailstone")
        }

        for (j in i + 1..<hailstones.size) {
            if (isIndependent(firstHailstone, hailstones[j]) && isIndependent(secondHailstone, hailstones[j])) {
                thirdHailstone = hailstones[j]
                println("found independent third hailstone $j: $thirdHailstone")
                break
            }
        }
        if (thirdHailstone == null) {
            throw IllegalStateException("could not find independent third hailstone")
        }

        val (rock, s) = findRock(firstHailstone, secondHailstone, thirdHailstone)
        println(rock)
        println(s)
        return ((rock.x + rock.y + rock.z) / s).toString()
    }

    private fun findRock(h1: Hailstone, h2: Hailstone, h3: Hailstone): Pair<Coord3D, BigDecimal> {
        val (a, A) = findPlane(h1, h2)
        val (b, B) = findPlane(h1, h3)
        val (c, C) = findPlane(h2, h3)

        val W = lin(A, cross(b, c), B, cross(c, a), C, cross(a, b))
        val t = dot(a, cross(b, c))
        val w = Coord3D(W.x / t, W.y / t, W.z / t)

        val w1 = sub(h1.velocity, w)
        val w2 = sub(h2.velocity, w)
        val ww = cross(w1, w2)

        val E = dot(ww, cross(h2.point, w2))
        val F = dot(ww, cross(h1.point, w1))
        val G = dot(h1.point, ww)
        val S = dot(ww, ww)

        val rock = lin(E, w1, -F, w2, G, ww)
        return rock to S
    }

    private fun isIndependent(a: Hailstone, b: Hailstone): Boolean {
        val cross = cross(a.velocity, b.velocity)
        return cross.x != BigDecimal.ZERO || cross.y != BigDecimal.ZERO || cross.z != BigDecimal.ZERO
    }

    private fun findPlane(a: Hailstone, b: Hailstone): Pair<Coord3D, BigDecimal> {
        val pointAB = sub(a.point, b.point)
        val velocityAB = sub(a.velocity, b.velocity)
        val vv = cross(a.velocity, b.velocity)
        return cross(pointAB, velocityAB) to dot(pointAB, vv)
    }

    private fun cross(a: Coord3D, b: Coord3D): Coord3D {
        return Coord3D(
            x = a.y * b.z - a.z * b.y,
            y = a.z * b.x - a.x * b.z,
            z = a.x * b.y - a.y * b.x,
        )
    }

    fun dot(a: Coord3D, b: Coord3D): BigDecimal {
        return a.x * b.x + a.y * b.y + a.z * b.z
    }

    private fun sub(a: Coord3D, b: Coord3D): Coord3D {
        return Coord3D(a.x - b.x, a.y - b.y, a.z - b.z)
    }

    private fun lin(r: BigDecimal, a: Coord3D, s: BigDecimal, b: Coord3D, t: BigDecimal, c: Coord3D): Coord3D {
        val x = r * a.x + s * b.x + t * c.x
        val y = r * a.y + s * b.y + t * c.y
        val z = r * a.z + s * b.z + t * c.z
        return Coord3D(x, y, z)
    }

    data class Coord3D(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal)

    data class Hailstone(val point: Coord3D, val velocity: Coord3D) {
        companion object {
            fun parseLine(str: String): Hailstone {
                val (coord, velocity) = str.split(" @ ")
                val (x, y, z) = coord.split(", ").map { BigDecimal(it.trim()) }
                val (xVelocity, yVelocity, zVelocity) = velocity.split(", ").map { BigDecimal(it.trim()) }
                return Hailstone(Coord3D(x, y, z), Coord3D(xVelocity, yVelocity, zVelocity))
            }
        }
    }
}