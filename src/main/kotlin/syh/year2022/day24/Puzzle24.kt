package syh.year2022.day24

import syh.AbstractAocDay
import syh.calculateLCM
import syh.library.Coord
import syh.library.Direction


class Puzzle24 : AbstractAocDay(2022, 24) {

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
        val rs = lines.size - 2
        val cs = lines[0].length - 2
        val lcm = calculateLCM(rs.toLong(), cs.toLong()).toInt()
        val freeStates = MutableList(lcm) { MutableList(rs) { MutableList(cs) { true } } }

        val blizzards = lines.drop(1).dropLast(1).flatMapIndexed { m, line ->
            line.drop(1).dropLast(1).mapIndexedNotNull { n, c ->
                if (c == '.') null else Blizzard(Coord(m, n), mapDirection(c.toString()))
            }
        }.toMutableList()

        for (i in 0..<lcm) {
            for (j in blizzards.indices) {
                val b = blizzards.removeAt(j)
                freeStates[i][b.coord.row][b.coord.column] = false
                blizzards.add(j, b.move(rs, cs))
            }
        }

        val start = Coord(-1, 0)
        val end = Coord(rs, cs - 1)
        val minutes = solve(start, end, 0, lcm, freeStates)
        println("took $minutes minutes to get from $start to $end")
        return minutes.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        val rs = lines.size - 2
        val cs = lines[0].length - 2
        val lcm = calculateLCM(rs.toLong(), cs.toLong()).toInt()
        val freeStates = MutableList(lcm) { MutableList(rs) { MutableList(cs) { true } } }

        val blizzards = lines.drop(1).dropLast(1).flatMapIndexed { m, line ->
            line.drop(1).dropLast(1).mapIndexedNotNull { n, c ->
                if (c == '.') null else Blizzard(Coord(m, n), mapDirection(c.toString()))
            }
        }.toMutableList()

        for (i in 0..<lcm) {
            for (j in blizzards.indices) {
                val b = blizzards.removeAt(j)
                freeStates[i][b.coord.row][b.coord.column] = false
                blizzards.add(j, b.move(rs, cs))
            }
        }

        val start = Coord(-1, 0)
        val end = Coord(rs, cs - 1)

        val startToEnd = solve(start, end, 0, lcm, freeStates)
        val endToStart = solve(end, start, startToEnd, lcm, freeStates)
        val startToEnd2 = solve(start, end, endToStart, lcm, freeStates)

        println("took $startToEnd minutes to get from $start to $end")
        println("took $endToStart minutes to get from $end to $start")
        println("took $startToEnd2 minutes to get from $start to $end")

        return startToEnd2.toString()
    }

    private fun solve(start: Coord, end: Coord, minutes: Int = 0, lcm: Int, freeStates: MutableList<MutableList<MutableList<Boolean>>>): Int {
        val queue = mutableListOf(State(start, minutes))
        val best = mutableMapOf<State, Int>()
        while (true) {
            val (coord, time) = queue.removeFirst()

            if (coord == end) {
                return time
            }

            val s = State(coord, time.mod(lcm))
            if (time >= (best[s] ?: Int.MAX_VALUE)) {
                continue
            } else {
                best[s] = time
            }

            val state = freeStates[(time + 1).mod(lcm)]
            if (coord == start || state[coord]) queue.add(State(coord, minute = time + 1))
            Direction.CARDINAL_DIRECTIONS
                .map { coord.relative(it) }
                .filter { it == end || state[it] }
                .forEach { queue.add(State(coord = it, minute = time + 1)) }
        }
    }

    private fun mapDirection(str: String): Direction {
        return when (str) {
            ">" -> Direction.EAST
            "v" -> Direction.SOUTH
            "<" -> Direction.WEST
            "^" -> Direction.NORTH
            else -> throw IllegalArgumentException("str $str is not a known direction ")
        }
    }

    data class State(val coord: Coord, val minute: Int)
    data class Blizzard(val coord: Coord, val dir: Direction) {
        fun move(rs: Int, cs: Int) = copy(
            coord = coord.relative(dir).mod(rs, cs)
        )
    }

    operator fun List<List<Boolean>>.get(coord: Coord) = coord.row in indices && coord.column in 0..<first().size && this[coord.row][coord.column]

}