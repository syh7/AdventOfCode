package syh.year2024.day22

import syh.AbstractAocDay


class Puzzle22 : AbstractAocDay(2024, 22) {
    override fun doA(file: String): String {
        val steps = if (file == "test1") 10 else 2000

        val memoiseMap = mutableMapOf<Long, Long>()

        val initialSeeds = readSingleLineFile(file).map { it.toLong() }
        val endSeeds = initialSeeds.map { seed ->
            var newSecret = seed
            for (i in 1..steps) {
                newSecret = calculateNextNumber(newSecret, memoiseMap)
//                println("calculated new secret $newSecret")
//                println()
            }
            newSecret
        }
        val totalSecret = endSeeds.sum()
        println("total for file $file is $totalSecret")
        return totalSecret.toString()
    }

    override fun doB(file: String): String {
        val steps = 2000
        val initialSeeds = readSingleLineFile(file).map { it.toLong() }

        val memoiseMap = mutableMapOf<Long, Long>()
        val changesMap = mutableMapOf<String, Long>()

        initialSeeds.forEach { seed ->
            val priceChanges = mutableListOf<Long>()
            val changesSeen = mutableSetOf<String>()

            var newSecret = seed
            var previousPrice = 0L

            for (i in 1..steps) {
                newSecret = calculateNextNumber(newSecret, memoiseMap)
                val newPrice = newSecret % 10

                val priceChange = newPrice - previousPrice
                previousPrice = newPrice

                priceChanges.add(priceChange)

                if (priceChanges.size > 4) {
                    priceChanges.removeFirst()

                    val key = createKey(priceChanges)
                    if (key !in changesSeen) {
                        changesMap.computeIfAbsent(key) { 0L }
                        changesMap[key] = changesMap[key]!! + newPrice
                        changesSeen.add(key)
                    }

                }
            }
        }

//        for ((key, total) in changesMap) {
//            println("key $key: $total")
//        }

        var max = 0L
        for ((key, total) in changesMap) {
            if (total > max) {
//                println("found new max string: $key with value $total")
                max = total
            }
        }
        return max.toString()
    }

    private fun calculateNextNumber(current: Long, mutableMap: MutableMap<Long, Long>): Long {
        if (mutableMap.containsKey(current)) {
            return mutableMap[current]!!
        }

        val mult = current * 64
        val multMixPrune = mixAndPrune(mult, current)
//        println("mult $mult and multmix $multMixPrune")

        val div = (multMixPrune / 32.0).toLong()
        val divMixPrune = mixAndPrune(div, multMixPrune)
//        println("div $div and divmix $divMixPrune")

        val mult2 = divMixPrune * 2048
        val mult2MixPrune = mixAndPrune(mult2, divMixPrune)
//        println("mult2 $mult2 and multmix2 $mult2MixPrune")

        mutableMap[current] = mult2MixPrune
        return mult2MixPrune
    }

    private fun mixAndPrune(given: Long, current: Long): Long {
        return given.xor(current) % 16777216
    }

    private fun createKey(list: List<Long>): String {
        return list.joinToString(",")
    }
}
