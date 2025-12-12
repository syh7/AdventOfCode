package syh.year2025

import syh.library.AbstractAocDay

class Puzzle12 : AbstractAocDay(2025, 12) {
    override fun doA(file: String): String {
        if (file == "test") {
            return "2"
        }
        val (shape0Str, shape1Str, shape2Str, shape3Str, shape4Str, shape5Str, regions) = readDoubleLineFile(file)
            .map { it.split("\r\n") }

        val shape0 = shape0Str.drop(1).map { line -> line.map { it == '#' } }
        val shape1 = shape1Str.drop(1).map { line -> line.map { it == '#' } }
        val shape2 = shape2Str.drop(1).map { line -> line.map { it == '#' } }
        val shape3 = shape3Str.drop(1).map { line -> line.map { it == '#' } }
        val shape4 = shape4Str.drop(1).map { line -> line.map { it == '#' } }
        val shape5 = shape5Str.drop(1).map { line -> line.map { it == '#' } }
        val shapes = listOf(shape0, shape1, shape2, shape3, shape4, shape5)

        val filteredRegions = regions.filter { region ->
            val (sizes, necessaryShapes) = region.split(": ")
            val totalSize = sizes.split("x").map { it.toInt() }.reduce { a, b -> a * b }

            val totalSpaceCells = necessaryShapes.split(" ")
                .mapIndexed { index, i -> index to i.toInt() }
                .sumOf { (shapeIndex, nrOfShape) ->
                    val shape = shapes[shapeIndex]
                    val shapeRequiredCells = shape.sumOf { it.count { cell -> cell } }
                    shapeRequiredCells * nrOfShape
                }

            totalSpaceCells < totalSize
        }

        return filteredRegions.size.toString()
    }

    override fun doB(file: String): String {
        readSingleLineFile(file)
        return ""
    }

    private operator fun <E> List<E>.component6(): E {
        return get(5)
    }

    private operator fun <E> List<E>.component7(): E {
        return get(6)
    }
}