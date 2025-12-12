package syh.year2021

import syh.library.AbstractAocDay

class Puzzle18 : AbstractAocDay(2021, 18) {
    override fun doA(file: String): String {
        return readSingleLineFile(file)
            .map { Snailfish(it) }
            .reduce { acc, new ->
                val snailfish = Snailfish("[${acc.str},${new.str}]")
                snailfish.reduce()
                snailfish
            }
            .calculateMagnitude()
            .toString()
    }

    override fun doB(file: String): String {
        val snailFishes = readSingleLineFile(file)
            .map { Snailfish(it) }
        var maxMagnitude = 0
        for (i in snailFishes.indices) {
            for (j in i + 1..<snailFishes.size) {
                val a = snailFishes[i]
                val b = snailFishes[j]
                val abFish = Snailfish("[${a.str},${b.str}]")
                abFish.reduce()
                val abMagnitude = abFish.calculateMagnitude()

                val baFish = Snailfish("[${b.str},${a.str}]")
                baFish.reduce()
                val baMagnitude = baFish.calculateMagnitude()
                maxMagnitude = maxOf(abMagnitude, baMagnitude, maxMagnitude)
            }
        }
        return maxMagnitude.toString()
    }

    data class Snailfish(var str: String) {

        fun calculateMagnitude(): Int {
            return generateSequence(str) { s ->
                s.replace("""\[(\d+),(\d+)]""".toRegex()) { match ->
                    val (left, right) = match.groupValues.drop(1).map { it.toInt() }
                    (3 * left + 2 * right).toString()
                }
            }.first { """\d+""".toRegex() matches it }.toInt()
        }

        fun reduce() {
            var performedReduction = true
            while (performedReduction) {
                performedReduction = explode() || split()
            }
        }

        private fun explode(): Boolean {
            val stack = mutableListOf<Int>()
            for ((index, char) in str.withIndex()) {
                when (char) {
                    '[' -> stack.add(index)
                    ']' -> {
                        val start = stack.removeLast()
                        if (stack.size >= 4) {
                            val (int1, int2) = str.substring(start + 1, index).split(',').map { it.toInt() }
                            val left = str.substring(0, start).addToLastInt(int1)
                            val right = str.substring(index + 1).addToFirstInt(int2)
                            str = "${left}0$right"
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun split(): Boolean {
            val match = """\d{2,}""".toRegex().findAll(str).firstOrNull()
            if (match == null) {
                return false
            } else {
                val left = match.value.toInt() / 2
                val right = match.value.toInt() / 2 + match.value.toInt() % 2
                str = str.replaceRange(match.range, "[$left,$right]")
                return true
            }
        }

        private fun String.addToLastInt(int: Int): String {
            val match = """\d+""".toRegex().findAll(this).lastOrNull()
            return if (match == null) this else {
                replaceRange(match.range, (match.value.toInt() + int).toString())
            }
        }

        private fun String.addToFirstInt(int: Int): String {
            val match = """\d+""".toRegex().findAll(this).firstOrNull()
            return if (match == null) this else {
                replaceRange(match.range, (match.value.toInt() + int).toString())
            }
        }

    }

}