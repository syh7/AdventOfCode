package syh.year2023.day12

import syh.AbstractAocDay


class Puzzle12 : AbstractAocDay(2023, 12) {
    private val SPRING = '#'
    private val EMPTY = '.'
    private val OPTIONAL = '?'

    override fun doA(file: String): String {
        val springArrangements = readSingleLineFile(file)
            .map { line ->
                val split = line.split(" ")
                SpringArrangement(split[0], split[1].split(",").map { it.toInt() }.toMutableList())
            }

        var total = 0L
        springArrangements.forEach {
            val sum = smartishDifferentSpringOptions(
                it.springInfo,
                it.expectedSprings,
                0,
                0,
                0,
                hashMapOf()
            )

            println("calculated sum for ${it.springInfo} is $sum")

            total += sum
        }

        println("total different arrangements for plain string: $total")
        return total.toString()
    }

    override fun doB(file: String): String {
        val springArrangements = readSingleLineFile(file)
            .map { line ->
                val split = line.split(" ")
                SpringArrangement(split[0], split[1].split(",").map { it.toInt() }.toMutableList())
            }

        var total = 0L
        springArrangements.forEach {
            val expandedArrangement = expand(it)
            val sum = smartishDifferentSpringOptions(
                expandedArrangement.springInfo,
                expandedArrangement.expectedSprings,
                0,
                0,
                0,
                hashMapOf()
            )

            println("calculated sum for expanded ${it.springInfo} is $sum")

            total += sum
        }

        println("total different arrangements for expanded string: $total")
        return total.toString()
    }

    private fun smartishDifferentSpringOptions(
        springString: String,
        expectedSpringCount: List<Int>,
        springIndex: Int,
        expectedCountIndex: Int,
        currentSpringGroupLength: Int,
        stateOptionsMap: HashMap<State, Long>
    ): Long {
        val currentState = State(springIndex, expectedCountIndex, currentSpringGroupLength)
        if (stateOptionsMap.contains(currentState)) {
            return stateOptionsMap[currentState]!!
        }

        // end of string reached
        if (springIndex == springString.length) {
            // counts as option if we also reached the end of the expected spring counts
            // or if the count index is the last expected count and current count is the same as the last expected count

            if (expectedCountIndex == expectedSpringCount.size && currentSpringGroupLength == 0) {
                return 1
            }
            if (expectedCountIndex == expectedSpringCount.size - 1 && currentSpringGroupLength == expectedSpringCount[expectedCountIndex]) {
                return 1
            }
            return 0
        }

        // end of string not yet reached, handle current character
        var total = 0L
        if (springString[springIndex] == EMPTY || springString[springIndex] == OPTIONAL) {
            // empty or optional char found
            // if no current group, just move on to next char
            // otherwise check if group size is correct, in that case continue
            if (currentSpringGroupLength == 0) {
                total += smartishDifferentSpringOptions(
                    springString,
                    expectedSpringCount,
                    springIndex + 1,
                    expectedCountIndex,
                    0,
                    stateOptionsMap
                )
            } else if (currentSpringGroupLength > 0 && expectedCountIndex < expectedSpringCount.size && expectedSpringCount[expectedCountIndex] == currentSpringGroupLength) {
                total += smartishDifferentSpringOptions(
                    springString,
                    expectedSpringCount,
                    springIndex + 1,
                    expectedCountIndex + 1,
                    0,
                    stateOptionsMap
                )
            }
        }

        if (springString[springIndex] == SPRING || springString[springIndex] == OPTIONAL) {
            // spring or optional char found
            // up current spring group and continue
            total += smartishDifferentSpringOptions(
                springString,
                expectedSpringCount,
                springIndex + 1,
                expectedCountIndex,
                currentSpringGroupLength + 1,
                stateOptionsMap
            )
        }

        // set state in map to prevent too many calculations
        stateOptionsMap[currentState] = total
        return total

    }

    private fun expand(arrangement: SpringArrangement): SpringArrangement {
        var expandedStr = arrangement.springInfo
        val expandedInt = ArrayList(arrangement.expectedSprings)
        for (i in 2..5) {
            expandedStr = expandedStr + OPTIONAL + arrangement.springInfo
            expandedInt.addAll(arrangement.expectedSprings)
        }
        return SpringArrangement(expandedStr, expandedInt)
    }

    data class SpringArrangement(var springInfo: String, val expectedSprings: MutableList<Int>)
    data class State(val springIndex: Int, val expectedCountIndex: Int, val currentSpringGroupLength: Int)
}