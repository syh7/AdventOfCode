package syh.year2024

import syh.library.AbstractAocDay

class Puzzle12 : AbstractAocDay(2024, 12) {
    override fun doA(file: String): String {
        println("starting file $file")
        val graph = readGraph(file)

        val regionMap = createRegionMap(graph)

        var total = 0
        regionMap.map { (k, v) ->
            println("region $k consists of ${v.size} separate regions")
            for (region in v) {
                println("\tregion with ${region.coords.size} coords: ${region.coords.map { it.toCoordString() }}")
                val area = region.area()
                val perimeter = region.perimeter()
                val subTotal = area * perimeter
                println("\tregion has area $area and perimeter $perimeter for total $subTotal")
                total += subTotal
            }
        }

        println()
        println()
        return total.toString()
    }

    override fun doB(file: String): String {
        println("starting file $file")
        val graph = readGraph(file)

        val regionMap = createRegionMap(graph)

        var total = 0
        regionMap.map { (k, v) ->
            println("region $k consists of ${v.size} separate regions")
            for (region in v) {
                println("\tregion with ${region.coords.size} coords: ${region.coords.map { it.toCoordString() }}")
                val area = region.area()
                val sides = region.sides()
                val subTotal = area * sides
                println("\tregion has area $area and sides $sides for total $subTotal")
                total += subTotal
            }
        }

        println()
        println()
        return total.toString()
    }

    private fun createRegionMap(graph: MutableList<MutableList<Coord>>): MutableMap<String, MutableList<Region>> {
        val regionMap = mutableMapOf<String, MutableList<Region>>()
        for (coord in graph.flatten()) {
            // find all relevant regions for coord
            regionMap.computeIfAbsent(coord.str) { mutableListOf(Region()) }
            val relevantRegions = regionMap[coord.str]!!.filter { it.contains(coord) }

            if (relevantRegions.isEmpty()) {
                // if no relevant region, create one
                val region = Region()
                region.coords.add(coord)
                regionMap[coord.str]!!.add(region)
            } else if (relevantRegions.size == 1) {
                // if
                relevantRegions[0].coords.add(coord)
            } else {
                // multiple regions found, add them together
                val combined = relevantRegions[0]
                combined.coords.add(coord)
                for (i in 1..<relevantRegions.size) {
                    combined.coords.addAll(relevantRegions[i].coords)
                }
                val notRelevant = regionMap[coord.str]!!.filterNot { it.contains(coord) }
                val left = mutableListOf(combined)
                left.addAll(notRelevant)
                regionMap[coord.str] = left
            }
        }
        return regionMap
    }

    private fun readGraph(file: String): MutableList<MutableList<Coord>> {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = mutableListOf<MutableList<Coord>>()
        for (j in chars.indices) {
            val row = mutableListOf<Coord>()
            for (i in chars[0].indices) {
                row.add(Coord(i, j, chars[j][i]))
            }
            graph.add(row)
        }

        for (row in graph) println(row.map { it.str })
        return graph
    }

    data class Region(val coords: MutableSet<Coord> = mutableSetOf()) {
        fun area(): Int = coords.size
        fun perimeter(): Int {
            var total = 0

            for (coord in coords) {
                total += 4
                val neighbours = coords.filter { coord.isNeighbour(it) }
//                println("coord ${coord.toCoordString()} has neighbours ${neighbours.map { it.toCoordString() }}")
                neighbours.forEach { _ -> total -= 1 }
            }
            return total
        }

        fun sides(): Int {
            // each side has 2 corners
            // each corners has 2 sides
            // so if we count corners, we indirectly count sides

            // there are outer and inner corners
            // see
            // OOOOO
            // OXOXO
            // OXXXO
            // the O on the top left is both an outer corner for LEFT and UP
            // it is also an inner corner for DOWN and RIGHT
            // coords can also be multiple corners, not just combination of outer and inner but also multiple outers or inners

            var corners = 0
            for (coord in coords) {
                println("checking coord $coord")
                // check outer corners
                corners += checkOuterCorners(coord)
                corners += checkInnerCorners(coord)
            }
            return corners
        }

        private fun checkOuterCorners(coord: Coord): Int {
            val north = coord.getDirection(Direction.NORTH)
            val east = coord.getDirection(Direction.EAST)
            val south = coord.getDirection(Direction.SOUTH)
            val west = coord.getDirection(Direction.WEST)

            var totalCorners = 0
            if (!coords.contains(north) && !coords.contains(east)) {
                println("north east corner")
                totalCorners += 1
            }
            if (!coords.contains(south) && !coords.contains(east)) {
                println("south east corner")
                totalCorners += 1
            }
            if (!coords.contains(north) && !coords.contains(west)) {
                println("north west corner")
                totalCorners += 1
            }
            if (!coords.contains(south) && !coords.contains(west)) {
                println("south west corner")
                totalCorners += 1
            }

            return totalCorners
        }

        private fun checkInnerCorners(coord: Coord): Int {
            val north = coord.getDirection(Direction.NORTH)
            val northEast = coord.getDirection(Direction.NORTH_EAST)
            val east = coord.getDirection(Direction.EAST)
            val southEast = coord.getDirection(Direction.SOUTH_EAST)
            val south = coord.getDirection(Direction.SOUTH)
            val southWest = coord.getDirection(Direction.SOUTH_WEST)
            val west = coord.getDirection(Direction.WEST)
            val northWest = coord.getDirection(Direction.NORTH_WEST)

            var innerCorners = 0
            if (coords.contains(north) && coords.contains(west) && !coords.contains(northWest)) {
                println("north west inner corner")
                innerCorners += 1
            }
            if (coords.contains(north) && coords.contains(east) && !coords.contains(northEast)) {
                println("north east inner corner")
                innerCorners += 1
            }
            if (coords.contains(south) && coords.contains(east) && !coords.contains(southEast)) {
                println("south east inner corner")
                innerCorners += 1
            }
            if (coords.contains(south) && coords.contains(west) && !coords.contains(southWest)) {
                println("south west inner corner")
                innerCorners += 1
            }
            return innerCorners
        }

        fun contains(coord: Coord): Boolean {
            return coords.isEmpty() || coords.any { it.isNeighbour(coord) }
        }
    }

    data class Coord(
        val x: Int, val y: Int, val str: String
    ) {

        fun getDirection(direction: Direction): Coord {
            return when (direction) {
                Direction.NORTH -> Coord(x, y - 1, str)
                Direction.NORTH_EAST -> Coord(x + 1, y - 1, str)
                Direction.EAST -> Coord(x + 1, y, str)
                Direction.SOUTH_EAST -> Coord(x + 1, y + 1, str)
                Direction.SOUTH -> Coord(x, y + 1, str)
                Direction.SOUTH_WEST -> Coord(x - 1, y + 1, str)
                Direction.WEST -> Coord(x - 1, y, str)
                Direction.NORTH_WEST -> Coord(x - 1, y - 1, str)
            }
        }

        fun isNeighbour(other: Coord): Boolean {
            return (this.x == other.x && (this.y == other.y - 1 || this.y == other.y + 1))
                    || (this.y == other.y && (this.x == other.x - 1 || this.x == other.x + 1))
        }

        fun toCoordString(): String {
            return "[$x][$y]"
        }
    }

    enum class Direction { NORTH, NORTH_WEST, WEST, SOUTH_WEST, SOUTH, SOUTH_EAST, EAST, NORTH_EAST }

}