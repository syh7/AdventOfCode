package syh.year2024.day19

import syh.AbstractAocDay


class Puzzle19 : AbstractAocDay(2024, 19) {
    override fun doA(file: String): String {
        val (a, b) = readDoubleLineFile(file)
        val towels = a.split(", ")
        val designs = b.split("\r\n")

        println("available towels: $towels")

        val map = mutableMapOf("" to true)
        val validDesigns = designs.count { validateDesigns(it, towels, map) }

        return validDesigns.toString()
    }

    override fun doB(file: String): String {
        val (a, b) = readDoubleLineFile(file)
        val towels = a.split(", ")
        val designs = b.split("\r\n")

        println("available towels: $towels")

        val map = mutableMapOf("" to 1L)
        val validDesigns = designs.sumOf { countOptions(it, towels, map) }

        return validDesigns.toString()
    }

    private fun validateDesigns(design: String, towels: List<String>, map: MutableMap<String, Boolean>): Boolean {
//        println("checking design $design")
        if (map.containsKey(design)) {
//            println("found in map: ${map[design]}")
            return map[design]!!
        }

//        println("checking towels")
        for (towel in towels) {
//            println("checking towel $towel for design $design")
            if (design.startsWith(towel)) {
                val valid = validateDesigns(design.removePrefix(towel), towels, map)
                if (valid) {
                    println("towel $towel is makes design $design valid")
                    map[design] = true
                    return true
                }
            }
        }
//        println("design $design is not valid")
        map[design] = false
        return false
    }

    private fun countOptions(design: String, towels: List<String>, map: MutableMap<String, Long>): Long {
//        println("checking design $design")
        if (map.containsKey(design)) {
//            println("found in map: ${map[design]}")
            return map[design]!!
        }

        map[design] = 0

//        println("checking towels")
        for (towel in towels) {
            if (design.startsWith(towel)) {
                val towelValue = countOptions(design.removePrefix(towel), towels, map)
//                println("towel $towel has $towelValue")
                map[design] = map[design]!! + towelValue
            }
        }
//        println("design $design has total value ${map[design]}")
        return map[design]!!
    }
}