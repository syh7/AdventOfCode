package syh.year2022.day20

import syh.AbstractAocDay


class Puzzle20 : AbstractAocDay(2022, 20) {

    override fun doA(file: String): String {
        val initialList = readSingleLineFile(file).map { it.toLong() }

        val indices = shuffle(initialList)

        val finalNumberOrdering = indices.associateWith { initialList[it] }.values.toList()
        println()
        println(finalNumberOrdering)

        return add100ths(finalNumberOrdering)
    }

    override fun doB(file: String): String {
        val decryptionKey = 811589153L
        val initialList = readSingleLineFile(file).map { it.toLong() * decryptionKey }

        val indices = shuffle(initialList, 10)

        val finalNumberOrdering = indices.associateWith { initialList[it] }.values.toList()
        println()
        println(finalNumberOrdering)

        return add100ths(finalNumberOrdering)
    }

    private fun add100ths(finalNumberOrdering: List<Long>): String {
        val zeroIndex = finalNumberOrdering.indexOf(0)

        val n1000 = finalNumberOrdering[(zeroIndex + 1000) % finalNumberOrdering.size]
        val n2000 = finalNumberOrdering[(zeroIndex + 2000) % finalNumberOrdering.size]
        val n3000 = finalNumberOrdering[(zeroIndex + 3000) % finalNumberOrdering.size]
        println("0 is found at $zeroIndex")
        println("n1000 is $n1000")
        println("n2000 is $n2000")
        println("n3000 is $n3000")
        return (n1000 + n2000 + n3000).toString()
    }

    private fun shuffle(initialList: List<Long>, shuffleTimes: Int = 1): MutableList<Int> {
        val indices = initialList.indices.toMutableList()
        println("initial:")
        println(initialList)

        for (j in 1..shuffleTimes) {
            for (i in initialList.indices) {
                val currentIndex = indices.indexOf(i)
                indices.removeAt(currentIndex)
                var newIndex = (currentIndex + initialList[i]) % (initialList.size - 1)
                if (newIndex < 0) newIndex += (initialList.size - 1)
                if (newIndex == 0L) newIndex = initialList.size - 1L
                indices.add(newIndex.toInt(), i)
            }
            println("after switching $j times")
            println(indices.associateWith { initialList[it] }.values.toList())
        }
        return indices
    }
}
