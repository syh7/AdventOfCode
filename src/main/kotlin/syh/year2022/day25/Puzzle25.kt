package syh.year2022.day25

import kotlin.math.pow
import syh.AbstractAocDay


class Puzzle25 : AbstractAocDay(2022, 25) {

    init {
        val conversions = listOf(
            1L to "1",
            2L to "2",
            3L to "1=",
            4L to "1-",
            5L to "10",
            6L to "11",
            7L to "12",
            8L to "2=",
            9L to "2-",
            10L to "20",
            15L to "1=0",
            20L to "1-0",
            2022L to "1=11-2",
            12345L to "1-0---0",
            314159265L to "1121-1110-1=0",
        )
        conversions.forEach { (dec, snafu) ->
            println("asserting $dec and $snafu")
            assert(decimalToSnafu(dec) == snafu)
            assert(snafuToDecimal(snafu) == dec)
        }
    }

    override fun doA(file: String): String {
        val snafuNumbers = readSingleLineFile(file)
        val totalDecimal = snafuNumbers.sumOf { snafu ->
            val subTotal = snafuToDecimal(snafu)
//            println("transformed $snafu into $subTotal")
            subTotal
        }
        println("total is $totalDecimal")

        val totalSnafu = decimalToSnafu(totalDecimal)
        println("total is $totalSnafu")

        return totalSnafu
    }

    private fun snafuToDecimal(snafu: String): Long {
        return snafu.reversed().mapIndexed { index, c ->
            val mul = when (c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException("c is not a valid snafu number")
            }
            val base = 5.0.pow(index)
            base * mul
        }
            .sum().toLong()
    }

    private fun decimalToSnafu(totalDecimal: Long): String {
        var leftover = totalDecimal
        var totalSnafu = ""
        while (leftover > 0) {
            val index = (leftover + 2) % 5
            totalSnafu += when (index) {
                0L -> "="
                1L -> "-"
                2L -> "0"
                3L -> "1"
                4L -> "2"
                else -> throw IllegalArgumentException("$index is not a snafu number")
            }
            if (index < 2) {
                leftover += 5
            }
            leftover /= 5
        }
        return totalSnafu.reversed()
    }

    override fun doB(file: String): String {
        return ""
    }

}