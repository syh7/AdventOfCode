package syh.year2021

import syh.library.AbstractAocDay

class Puzzle14 : AbstractAocDay(2021, 14) {
    override fun doA(file: String): String {
        val (initial, conversionStrings) = readDoubleLineFile(file)
        val conversions = conversionStrings.split("\r\n").associate {
            val (pair, insert) = it.split(" -> ")
            pair to insert
        }
        println(conversions)

        var workString = initial
        for (step in 1..10) {
            var newString = ""
            for (i in 1..<workString.length) {
                val strToConvert = workString.substring(i - 1, i + 1)
                val insertStr = conversions[strToConvert] ?: throw IllegalArgumentException("cannot find $strToConvert in conversions")
                newString += strToConvert[0]
                newString += insertStr
            }
            newString += workString.last()
            println("Step $step made $workString to $newString")
            workString = newString
        }

        val distinctChars = workString.toList().distinct()
        val occurrences = distinctChars.associateWith { char -> workString.count { it == char } }
        val max = occurrences.values.max()
        val min = occurrences.values.min()

        println(occurrences)

        return (max - min).toString()
    }

    override fun doB(file: String): String {
        val (initialStr, conversionStrings) = readDoubleLineFile(file)
        val conversions = conversionStrings.split("\r\n").associate {
            val (pair, insert) = it.split(" -> ")
            pair to listOf("${pair[0]}$insert", "$insert${pair[1]}")
        }
        println(conversions)

        val startPolymer = initialStr.windowed(2).groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
        println(startPolymer)

        var polymer = startPolymer
        for (i in 1..40) {
            polymer = polymer.asSequence().fold(mutableMapOf()) { combinedMap, (pair, count) ->
                val replacements = conversions[pair] ?: throw IllegalArgumentException("cannot find $pair in conversions")
                replacements.forEach {
                    combinedMap.merge(it, count) { prev, next -> prev + next }
                }
                combinedMap
            }
            println("After step $i, polymer is $polymer")
        }

        val occurrences = polymer.asSequence().fold(mutableMapOf<Char, Long>()) { combinedMap, (pair, count) ->
            pair.forEach { combinedMap.merge(it, count) { prev, next -> prev + next } }
            combinedMap
        }
            // we double our numbers because we split NNC into NN and NC, so now we need to halve them
            .map { (char, count) -> char to if (count % 2 == 0L) count / 2 else (count + 1) / 2 }
            .toMap()
        val max = occurrences.values.max()
        val min = occurrences.values.min()
        println(occurrences)


        return (max - min).toString()
    }

}