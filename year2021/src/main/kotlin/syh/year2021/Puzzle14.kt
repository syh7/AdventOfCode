package syh.year2021

import syh.library.AbstractAocDay

class Puzzle14 : AbstractAocDay(2021, 14) {
    override fun doA(file: String): String {
        val (initialStr, conversionStrings) = readDoubleLineFile(file)

        val startPolymer = readPolymer(initialStr)
        val conversions = createConversionMap(conversionStrings)

        val polymer = performPolymerSteps(startPolymer, conversions, 10)
        val occurrencesMap = countCharOccurrences(polymer)

        return (occurrencesMap.values.max() - occurrencesMap.values.min()).toString()
    }

    override fun doB(file: String): String {
        val (initialStr, conversionStrings) = readDoubleLineFile(file)

        val startPolymer = readPolymer(initialStr)
        val conversions = createConversionMap(conversionStrings)

        val polymer = performPolymerSteps(startPolymer, conversions, 40)
        val occurrencesMap = countCharOccurrences(polymer)

        return (occurrencesMap.values.max() - occurrencesMap.values.min()).toString()
    }

    private fun performPolymerSteps(startPolymer: Map<String, Long>, conversions: Map<String, List<String>>, amountOfSteps: Int): Map<String, Long> {
        var polymer = startPolymer
        for (i in 1..amountOfSteps) {
            polymer = polymer.asSequence().fold(mutableMapOf()) { combinedMap, (pair, count) ->
                val replacements = conversions[pair] ?: throw IllegalArgumentException("cannot find $pair in conversions")
                replacements.forEach {
                    combinedMap.merge(it, count) { prev, next -> prev + next }
                }
                combinedMap
            }
            println("After step $i, polymer is $polymer")
        }
        return polymer
    }

    private fun countCharOccurrences(polymer: Map<String, Long>): Map<Char, Long> {
        val occurrences = polymer.asSequence().fold(mutableMapOf<Char, Long>()) { combinedMap, (pair, count) ->
            pair.forEach { combinedMap.merge(it, count) { prev, next -> prev + next } }
            combinedMap
        }
            // we double our numbers because we split NNC into NN and NC, so now we need to halve them
            .map { (char, count) -> char to if (count % 2 == 0L) count / 2 else (count + 1) / 2 }
            .toMap()
        println(occurrences)
        return occurrences
    }

    private fun readPolymer(initialStr: String): Map<String, Long> {
        val startPolymer = initialStr.windowed(2).groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
        println(startPolymer)
        return startPolymer
    }

    private fun createConversionMap(conversionStrings: String): Map<String, List<String>> {
        val conversions = conversionStrings.split("\r\n").associate {
            val (pair, insert) = it.split(" -> ")
            pair to listOf("${pair[0]}$insert", "$insert${pair[1]}")
        }
        println(conversions)
        return conversions
    }
}